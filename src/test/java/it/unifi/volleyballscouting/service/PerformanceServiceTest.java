package it.unifi.volleyballscouting.service;

import static it.unifi.volleyballscouting.model.TestData.*;

import it.unifi.volleyballscouting.dto.scouting.ScoutPageDTO;
import it.unifi.volleyballscouting.dto.scouting.ScoutRowDTO;
import it.unifi.volleyballscouting.dto.scouting.ScoutSetFormDTO;
import it.unifi.volleyballscouting.exception.BusinessRuleException;
import it.unifi.volleyballscouting.model.*;
import it.unifi.volleyballscouting.repository.PerformanceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class PerformanceServiceTest {
	@Mock PerformanceRepository perfRepo;
	@Mock MatchService matchService;
	@Mock CoachService coachService;
	@Mock PlayerService playerService;

	@InjectMocks PerformanceService performanceService;

	@Test
	void coachNotHavingTeam(){
		Match m = newMatch();
		when(matchService.findById(m.getId())).thenReturn(m);
		Coach c = coachNoTeam(); //all methods from TestData
		when(coachService.getCoachTeam(c.getId())).thenReturn(null);
		assertThatThrownBy(() -> performanceService.getScoutPage(m.getId(), c.getId())).isInstanceOf(BusinessRuleException.class)
				.hasMessageContaining("Non hai ancora una squadra assegnata");

	}

	@Test
	void coachHavingWrongTeam(){
		Match m = newMatch();
		when(matchService.findById(m.getId())).thenReturn(m);
		Coach c = coachWithTeam();
		Team t = c.getTeam();
		when(coachService.getCoachTeam(c.getId())).thenReturn(t);//with when you have to call the method that will return the value in the tested one
		assertThatThrownBy(() -> performanceService.getScoutPage(m.getId(), c.getId())).isInstanceOf(AccessDeniedException.class)
				.hasMessageContaining("La tua squadra non gioca questa partita");

	}

	@Test
	void matchNotStarted(){
		Match m = newMatch();
		when(matchService.findById(m.getId())).thenReturn(m);
		Team t = m.getTeamHome();
		Coach c = m.getTeamHome().getCoach();
		when(coachService.getCoachTeam(c.getId())).thenReturn(t); //needed all the components in the tested method
		assertThatThrownBy(() -> performanceService.getScoutPage(m.getId(), c.getId())).isInstanceOf(BusinessRuleException.class)
				.hasMessageContaining("La partita non è ancora iniziata");
	}

	@ParameterizedTest
	@ValueSource(booleans = {false, true})
	void pageSentOrNot(boolean alreadySent){
		Match m = startedMatch();
		Team t = m.getTeamHome();
		Coach c = m.getTeamHome().getCoach();
		GameSet current = assignId(m.getCurrentSet());
		when(matchService.findById(m.getId())).thenReturn(m);
		when(coachService.getCoachTeam(c.getId())).thenReturn(t);
		when(perfRepo.existsByTeamIdAndGameSetId(t.getId(), current.getId())).thenReturn(alreadySent);
		ScoutPageDTO page = performanceService.getScoutPage(m.getId(), c.getId());
		assertThat(page.alreadySent()).isEqualTo(alreadySent);
	}

	@Test
	void saveScoutingWithWrongSet(){
		Match m = startedMatch();
		Coach c = m.getTeamHome().getCoach();
		Team t = c.getTeam();
		when(matchService.findById(m.getId())).thenReturn(m);
		when(coachService.getCoachTeam(c.getId())).thenReturn(t);
		ScoutSetFormDTO form = new ScoutSetFormDTO(List.of());
		assertThatThrownBy(() -> performanceService.saveSetScouting(m.getId(), c.getId(), 2, form)).isInstanceOf(BusinessRuleException.class)
				.hasMessageContaining("Il set indicato non esiste");
	}
	@Test
	void saveSetReSending(){
		Match m = startedMatch();
		Coach c = m.getTeamHome().getCoach();
		Team t = c.getTeam();
		GameSet current = assignId(m.getCurrentSet());
		when(matchService.findById(m.getId())).thenReturn(m);
		when(coachService.getCoachTeam(c.getId())).thenReturn(t);
		ScoutSetFormDTO form = new ScoutSetFormDTO(List.of());
		when(perfRepo.existsByTeamIdAndGameSetId(t.getId(),current.getId())).thenReturn(true);
		assertThatThrownBy(() -> performanceService.saveSetScouting(m.getId(), c.getId(), current.getSetNumber(), form)).isInstanceOf(BusinessRuleException.class)
				.hasMessageContaining("Il risultato è già stato inviato");
	}

	@Test
	void saveSetDuplicatedPlayer(){
		Match m = startedMatch();
		Coach c = m.getTeamHome().getCoach();
		Team t = c.getTeam();
		GameSet current = assignId(m.getCurrentSet());
		when(matchService.findById(m.getId())).thenReturn(m);
		when(coachService.getCoachTeam(c.getId())).thenReturn(t);
		Player p1 = assignId(new Player ("test1", "test2", "test3", "test4", LocalDate.of(1990, 12, 10), 20, PlayerRole.ALZATORE));
		p1.setTeam(t);
		when(playerService.findOwnedBy(p1.getId(), t.getId())).thenReturn(p1);
		ScoutSetFormDTO form = formWithRow(p1,p1);
		when(perfRepo.existsByTeamIdAndGameSetId(t.getId(),current.getId())).thenReturn(false);
		assertThatThrownBy(() -> performanceService.saveSetScouting(m.getId(), c.getId(), current.getSetNumber(), form)).isInstanceOf(BusinessRuleException.class)
				.hasMessageContaining("Giocatore ripetuto nel set");
	}
	@Test
	void saveSetWithPlayerOfAnotherTeam(){
		Match m = startedMatch();
		Coach c = m.getTeamHome().getCoach();
		Team t = c.getTeam();
		GameSet current = assignId(m.getCurrentSet());
		when(matchService.findById(m.getId())).thenReturn(m);
		when(coachService.getCoachTeam(c.getId())).thenReturn(t);
		when(perfRepo.existsByTeamIdAndGameSetId(t.getId(), current.getId())).thenReturn(false);

		Player stranger = assignId(new Player("test1", "test2", "test3", "test4", LocalDate.of(1990, 12, 10), 20, PlayerRole.ALZATORE));
		stranger.setTeam(m.getTeamGuest());   // giocatore della squadra avversaria
		when(playerService.findOwnedBy(stranger.getId(), t.getId()))
				.thenThrow(new AccessDeniedException("Il giocatore non appartiene alla tua squadra"));

		ScoutRowDTO row = new ScoutRowDTO(stranger.getId(), 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		ScoutSetFormDTO form = new ScoutSetFormDTO(List.of(row));

		assertThatThrownBy(() -> performanceService.saveSetScouting(m.getId(), c.getId(), current.getSetNumber(), form))
				.isInstanceOf(AccessDeniedException.class)
				.hasMessageContaining("non appartiene alla tua squadra");
		verify(perfRepo, never()).save(any());
	}

	@Test
	void saveSetWithEmptyRow(){
		Match m = startedMatch();
		Coach c = m.getTeamHome().getCoach();
		Team t = c.getTeam();
		GameSet current = assignId(m.getCurrentSet());
		when(matchService.findById(m.getId())).thenReturn(m);
		when(coachService.getCoachTeam(c.getId())).thenReturn(t);
		Player p1 = assignId(new Player ("test1", "test2", "test3", "test4", LocalDate.of(1990, 12, 10), 20, PlayerRole.ALZATORE));
		Player p2 = assignId(new Player ("test10", "test11", "test12", "test13", LocalDate.of(1990, 12, 10), 24, PlayerRole.SCHIACCIATORE_OPPOSTO));
		p1.setTeam(t);
		p2.setTeam(t);
		when(playerService.findOwnedBy(p1.getId(), t.getId())).thenReturn(p1);
		ScoutRowDTO full = new ScoutRowDTO(p1.getId(), 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		ScoutRowDTO empty = ScoutRowDTO.zero(p2.getId());
		ScoutSetFormDTO form = new ScoutSetFormDTO(List.of(full, empty));
		when(perfRepo.existsByTeamIdAndGameSetId(t.getId(),current.getId())).thenReturn(false);
		performanceService.saveSetScouting(m.getId(), c.getId(), current.getSetNumber(), form);

		ArgumentCaptor<Performance> captor = ArgumentCaptor.forClass(Performance.class);
		verify(perfRepo, times(1)).save(captor.capture());
		List<Performance> saved = captor.getAllValues();
		assertThat(saved).extracting(Performance::getGameSet).containsOnly(current);
		assertThat(saved).extracting(Performance::getTeam).containsOnly(t);
		assertThat(saved).extracting(p -> p.getPlayer().getId()).containsExactlyInAnyOrder(p1.getId());
	}

	@Test
	void saveSetCorrectly(){
		Match m = startedMatch();
		Coach c = m.getTeamHome().getCoach();
		Team t = c.getTeam();
		GameSet current = assignId(m.getCurrentSet());
		when(matchService.findById(m.getId())).thenReturn(m);
		when(coachService.getCoachTeam(c.getId())).thenReturn(t);

		Player p1 = assignId(new Player ("test1", "test2", "test3", "test4", LocalDate.of(1990, 12, 10), 20, PlayerRole.ALZATORE));
		Player p2 = assignId(new Player ("test10", "test11", "test12", "test13", LocalDate.of(1990, 12, 10), 24, PlayerRole.SCHIACCIATORE_OPPOSTO));
		p1.setTeam(t);
		p2.setTeam(t);
		when(playerService.findOwnedBy(p1.getId(), t.getId())).thenReturn(p1);
		when(playerService.findOwnedBy(p2.getId(),t.getId())).thenReturn(p2);

		ScoutSetFormDTO form = formWithRow(p1,p2);
		when(perfRepo.existsByTeamIdAndGameSetId(t.getId(),current.getId())).thenReturn(false);
		performanceService.saveSetScouting(m.getId(), c.getId(), current.getSetNumber(), form);

		ArgumentCaptor<Performance> captor = ArgumentCaptor.forClass(Performance.class);
		verify(perfRepo, times(2)).save(captor.capture());
		List<Performance> saved = captor.getAllValues();
		assertThat(saved).extracting(Performance::getGameSet).containsOnly(current);
		assertThat(saved).extracting(Performance::getTeam).containsOnly(t);
		assertThat(saved).extracting(p -> p.getPlayer().getId()).containsExactlyInAnyOrder(p1.getId(), p2.getId());
	}




}
