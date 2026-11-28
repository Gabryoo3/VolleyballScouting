package it.unifi.volleyballscouting.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static it.unifi.volleyballscouting.model.TestData.*;

import static org.assertj.core.api.Assertions.assertThat;

//Covered tests:
//see limit case sets, both finished and not (done)
// see limit 5th set case: 14-15, 16-15, 17-15
// see if there can be a set with more than 25 points, or 15 in 5th set case (done)
// test some non-finished sets (done)
public class GameSetTest {

	@Test
	void finishedLimitsSet() {
		Match m = startedMatch();

		GameSet g1 = new GameSet(m, 1);
		g1.setTeamHomePoints(26);
		g1.setTeamGuestPoints(24);

		assertThat(g1.isFinished()).isTrue();

		GameSet g2 = new GameSet(m, 2);
		g2.setTeamHomePoints(26);
		g2.setTeamGuestPoints(27);

		assertThat(g2.isFinished()).isFalse();

		g2.setTeamGuestPoints(28);

		assertThat(g2.isFinished()).isTrue();

		GameSet g5 = new GameSet(m, 5);
		g5.setTeamHomePoints(15);
		g5.setTeamGuestPoints(13);

		assertThat(g5.isFinished()).isTrue();
	}

	@Test
	void limitCase5thSet() {
		Match m = startedMatch();
		GameSet g5 = new GameSet(m, 5);
		g5.setTeamGuestPoints(15);
		// 14-15
		g5.setTeamHomePoints(14);
		assertThat(g5.isFinished()).isFalse();
		// 16-15
		g5.setTeamHomePoints(16);
		assertThat(g5.isFinished()).isFalse();
		// 17-15
		g5.setTeamHomePoints(17);
		assertThat(g5.isFinished()).isTrue();
	}

	@Test
	void notFinishedByScoreSets() {
		Match m = startedMatch();

		GameSet g1 = new GameSet(m, 1);
		g1.setTeamHomePoints(25);
		g1.setTeamGuestPoints(25);
		assertThat(g1.isFinished()).isFalse();

		g1.setTeamHomePoints(30);
		g1.setTeamGuestPoints(10);
		assertThat(g1.isFinished()).isFalse();

		g1.setTeamHomePoints(25);
		g1.setTeamGuestPoints(24);
		assertThat(g1.isFinished()).isFalse();

		GameSet g5 = new GameSet(m, 5);
		g5.setTeamHomePoints(25);
		g5.setTeamGuestPoints(13);
		assertThat(g5.isFinished()).isFalse();
	}

	@ParameterizedTest
	@CsvSource({
			"1, 25, 10",
			"1, 25, 23",
			"1, 25, 0",
			"1, 13, 25",
			"5, 15, 4",
			"5, 15, 13",
			"5, 12, 15"
	})
	void winningSets(int setNumber, int homePoints, int guestPoints) {
		Match m = startedMatch();
		GameSet g = new GameSet(m, setNumber);
		g.setTeamHomePoints(homePoints);
		g.setTeamGuestPoints(guestPoints);
		assertThat(g.isFinished()).isTrue();
	}

}
