package it.unifi.volleyballscouting.service;

import static it.unifi.volleyballscouting.model.TestData.*;
import it.unifi.volleyballscouting.exception.BusinessRuleException;
import it.unifi.volleyballscouting.model.*;
import it.unifi.volleyballscouting.repository.PerformanceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;


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

}
