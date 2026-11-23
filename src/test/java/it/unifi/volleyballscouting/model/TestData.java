package it.unifi.volleyballscouting.model;

import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;


public class TestData {
	public static <T extends BaseModel> T assignId(T entity){
		ReflectionTestUtils.setField(entity, "id", UUID.randomUUID());
		return entity;
	}

	public static Match startedMatch() {
		Match m = newMatch();
		m.start();
		return m;
	}

	public static Match newMatch() {
		Address homeAddress = new Address("Roma", "Firenze", "50555");
		Address guestAddress = new Address("Napoli", "Prato", "59100");
		Coach homeCoach = assignId(new Coach("Giulio", "Bianchi", "giubia", "test1234", LocalDate.of(1990, 10, 23)));
		Coach guestCoach = assignId(new Coach("Alessio", "Rossini", "aleros", "test1234", LocalDate.of(1984, 3, 15)));
		Referee ref = assignId(new Referee("Giulio", "Romanotti", "giuromanref", "test4321", LocalDate.of(1970, 5, 23)));
		Team home = assignId(new Team("CasaRosa", homeAddress, homeCoach));
		Team guest = assignId( new Team("Hohahola", guestAddress, guestCoach));
		return assignId( new Match(home, guest, LocalDateTime.now(), ref));
	}

	public static Coach coachNoTeam(){
		return assignId(new Coach("test1", "test2", "test3", "test4", LocalDate.of(1970, 11, 23)));
	}

	public static Coach coachWithTeam(){
		Coach c = coachNoTeam();
		Address a = new Address("test1", "test2", "test3");
		Team t = assignId(new Team("test", a, c));
		c.setTeam(t);
		return c;
	}
}
