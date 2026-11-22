package it.unifi.volleyballscouting.model;

import it.unifi.volleyballscouting.exception.BusinessRuleException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MatchTest {

	private Match startedMatch(){
		Address homeAddress = new Address("Roma", "Firenze", "50555");
		Address guestAddress = new Address("Napoli", "Prato", "59100");
		Coach homeCoach = new Coach("Giulio", "Bianchi", "giubia", "test1234", LocalDate.of(1990, 10, 23));
		Coach guestCoach = new Coach("Alessio", "Rossini", "aleros", "test1234", LocalDate.of(1984, 3, 15));
		Referee ref = new Referee("Giulio", "Romanotti", "giuromanref", "test4321", LocalDate.of(1970,5,23));
		Team home = new Team("CasaRosa", homeAddress, homeCoach);
		Team guest = new Team ("Hohahola", guestAddress, guestCoach);
		Match match = new Match (home, guest, LocalDateTime.now(), ref);
		match.start();
		return match;

	}

	@Test
	void finishMatchAfterThreeWins(){ //move to MatchTest
		Match m = startedMatch();

		m.closeCurrentSet(25,12);
		m.closeCurrentSet(25,10);
		m.closeCurrentSet(25,9);

		assertThat(m.isFinished()).isTrue();
		assertThat(m.getHomeScore()).isEqualTo(3);
	}

	@Test
	void correctWinner(){
		Match m1 = startedMatch();
		m1.closeCurrentSet(25,23);
		m1.closeCurrentSet(25,23);
		m1.closeCurrentSet(25,23);
		assertThat(m1.getWinner()).isEqualTo(m1.getTeamHome());

		Match m2 = startedMatch();
		m2.closeCurrentSet(13,25);
		m2.closeCurrentSet(13,25);
		m2.closeCurrentSet(13,25);

		assertThat(m2.getWinner()).isEqualTo(m2.getTeamGuest());
	}

	@Test
	void finishedLimitsSet() {
		Match m = startedMatch();

		GameSet g1 = new GameSet(m, 1);
		g1.setTeamHomePoints(26);
		g1.setTeamGuestPoints(24);

		assertThat(g1.isFinished()).isTrue();

		m.closeCurrentSet(26, 24);

		GameSet g2 = new GameSet(m, 2);
		g2.setTeamHomePoints(26);
		g2.setTeamGuestPoints(27);

		assertThat(g2.isFinished()).isFalse();

		g2.setTeamGuestPoints(28);

		assertThat(g2.isFinished()).isTrue();

		m.closeCurrentSet(26, 28);

		m.closeCurrentSet(25, 23);
		m.closeCurrentSet(13, 25);

		GameSet g5 = new GameSet(m, 5);
		g5.setTeamHomePoints(15);
		g5.setTeamGuestPoints(13);

		assertThat(g5.isFinished()).isTrue();
		m.closeCurrentSet(15, 13);

		assertThat(m.isFinished()).isTrue();
	}

	@Test
	void limitCase5thSet() {
		Match m = startedMatch();
		m.closeCurrentSet(25, 13);
		m.closeCurrentSet(25, 11);
		m.closeCurrentSet(14, 25);
		m.closeCurrentSet(12, 25);
		assertThat(m.isFinished()).isFalse();
		GameSet g5 = new GameSet(m, 5);
		g5.setTeamHomePoints(16);
		g5.setTeamGuestPoints(15);
		assertThat(g5.isFinished()).isFalse();
		g5.setTeamHomePoints(17);
		assertThat(g5.isFinished()).isTrue();

		m.closeCurrentSet(17, 15);
		assertThat(m.isFinished()).isTrue();
	}

	@Test
	void setWithMore25and15on5th() {
		Match m = startedMatch();
		assertThatThrownBy(() -> m.closeCurrentSet(26, 14)).isInstanceOf(BusinessRuleException.class);
		m.closeCurrentSet(25, 13);
		m.closeCurrentSet(25, 12);
		m.closeCurrentSet(13, 25);
		m.closeCurrentSet(14, 25);

		assertThatThrownBy(() -> m.closeCurrentSet(18, 14)).isInstanceOf(BusinessRuleException.class);
	}
}
