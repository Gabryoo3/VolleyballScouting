package it.unifi.volleyballscouting.model;

import it.unifi.volleyballscouting.exception.BusinessRuleException;
import org.junit.jupiter.api.Test;
import static it.unifi.volleyballscouting.model.TestData.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MatchTest {


	@Test
	void finishMatchAfterThreeWins() {
		Match m = newMatch();
		m.start();
		m.closeCurrentSet(25, 12);
		m.closeCurrentSet(25, 10);
		m.closeCurrentSet(25, 9);

		assertThat(m.isFinished()).isTrue();
		assertThat(m.getHomeScore()).isEqualTo(3);
	}

	@Test
	void matchNotFinishedOver3rdSet() {
		Match m = newMatch();
		m.start();
		m.closeCurrentSet(25, 12);
		m.closeCurrentSet(25, 10);
		m.closeCurrentSet(13, 25);
		assertThat(m.isFinished()).isFalse();
		m.closeCurrentSet(25, 14);
		assertThat(m.isFinished()).isTrue();
	}

	@Test
	void getWinnerOnNotFinishedMatch() {
		Match m = newMatch();
		m.start();

		m.closeCurrentSet(25, 10);
		assertThatThrownBy(m::getWinner).isInstanceOf(BusinessRuleException.class);
	}

	@Test
	void checkCorrectPointAssignment() {
		Match m = newMatch();
		m.start();
		m.closeCurrentSet(25, 14);
		GameSet g1 = m.getSets().getFirst();
		assertThat(g1.getTeamHomePoints()).isEqualTo(25);
		assertThat(g1.getTeamGuestPoints()).isEqualTo(14);
	}

	@Test
	void checkNewSetCorrectlyOpened() {
		Match m = newMatch();
		m.start();
		m.closeCurrentSet(25, 14);
		assertThat(m.getCurrentSet().getSetNumber()).isEqualTo(2);
	}

	@Test
	void correctWinner() {
		Match m1 = newMatch();
		m1.start();
		m1.closeCurrentSet(25, 23);
		m1.closeCurrentSet(25, 23);
		m1.closeCurrentSet(25, 23);
		assertThat(m1.getWinner()).isSameAs(m1.getTeamHome());

		Match m2 = newMatch();
		m2.start();
		m2.closeCurrentSet(13, 25);
		m2.closeCurrentSet(13, 25);
		m2.closeCurrentSet(13, 25);

		assertThat(m2.getWinner()).isSameAs(m2.getTeamGuest());
	}

	@Test
	void matchWith5Sets() {
		Match m = newMatch();
		m.start();
		m.closeCurrentSet(25, 13);
		m.closeCurrentSet(25, 11);
		m.closeCurrentSet(14, 25);
		m.closeCurrentSet(12, 25);
		assertThat(m.isFinished()).isFalse();
		m.closeCurrentSet(17, 15);
		assertThat(m.getCurrentSet().getSetNumber()).isEqualTo(5);
		assertThat(m.getHomeScore()).isEqualTo(3);
		assertThat(m.isFinished()).isTrue();
	}

	@Test
	void setWithMore25and15on5th() {
		Match m = newMatch();
		m.start();

		assertThatThrownBy(() -> m.closeCurrentSet(26, 14)).isInstanceOf(BusinessRuleException.class);
		assertThat(m.getCurrentSet().getSetNumber()).isEqualTo(1);
		assertThat(m.getHomeScore()).isEqualTo(0);
		assertThat(m.getGuestScore()).isEqualTo(0);
		assertThat(m.getCurrentSet().getTeamHomePoints()).isEqualTo(0);
		assertThat(m.getCurrentSet().getTeamGuestPoints()).isEqualTo(0);

		m.closeCurrentSet(25, 13);
		m.closeCurrentSet(25, 12);
		m.closeCurrentSet(13, 25);
		m.closeCurrentSet(14, 25);

		assertThatThrownBy(() -> m.closeCurrentSet(18, 14)).isInstanceOf(BusinessRuleException.class);
		assertThat(m.getHomeScore()).isEqualTo(2);
		assertThat(m.getGuestScore()).isEqualTo(2);
		assertThat(m.getCurrentSet().getSetNumber()).isEqualTo(5);
		assertThat(m.getCurrentSet().getTeamHomePoints()).isEqualTo(0);
		assertThat(m.getCurrentSet().getTeamGuestPoints()).isEqualTo(0);
	}

	@Test
	void closeSetOnNotStartedMatch() {
		Match m = newMatch();
		assertThatThrownBy(() -> m.closeCurrentSet(25, 10)).isInstanceOf(BusinessRuleException.class)
				.hasMessageContaining("La partita non è ancora iniziata");
	}

	@Test
	void startMatchTwice() {
		Match m = newMatch();
		m.start();
		assertThatThrownBy(m::start).isInstanceOf(BusinessRuleException.class)
				.hasMessageContaining("La partita è già avviata");
	}

	@Test
	void closeSetAfterMatchFinished() {
		Match m = newMatch();
		m.start();
		m.closeCurrentSet(25, 13);
		m.closeCurrentSet(25, 13);
		m.closeCurrentSet(25, 13);
		assertThatThrownBy(() -> m.closeCurrentSet(25, 13)).isInstanceOf(BusinessRuleException.class)
				.hasMessageContaining("La partita è già conclusa");
		assertThat(m.getHomeScore()).isEqualTo(3);
		assertThat(m.getGuestScore()).isEqualTo(0);
	}

}
