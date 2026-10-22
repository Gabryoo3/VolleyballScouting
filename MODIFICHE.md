# Registro delle modifiche — VolleyballScouting

> Documento vivo: elenca e argomenta tutte le modifiche apportate al progetto durante
> l'integrazione del front-end e la messa in funzione dell'applicazione.
> Viene aggiornato man mano che il lavoro procede.

**Ultimo aggiornamento:** 22/10/2026
**Contesto:** branch `front-end_try`. Obiettivo: rendere l'applicazione avviabile e
navigabile (database, pagine, bottoni), correggendo gli errori che impedivano compilazione,
avvio e login, realizzando i template Thymeleaf e impostando l'architettura dei ruoli.

> **Nota (26/08/2026):** dopo un rollback, rifattorizzati data-layer e sicurezza: id delle
> entità in `UUID` (`GenerationType.UUID` in `BaseModel`), autenticazione via `AppUserDetails`
> risolto da `AppUserDetailsService`, ruoli convertiti con `PlayerRole.valueOf(...)`.

> **Nota (14/09/2026):** a valle del ricevimento col professore, revisione del **domain model**
> (Gaphor) e delle scelte progettuali (sezione 10): Address embeddable, owner Coach + `mappedBy`,
> metodi `assign`, entità `Admin`, rinomina `Set`→`GameSet`, fix getter `Performance`, pulizia
> `security/a`, fix `birthdate` nel seeder.

> **Nota (05/10/2026):** consolidata l'**architettura "Soluzione A"** (separazione Admin/Coach,
> sezione 11), irrobustite le guardie nei controller, rifatto `players/form.html`, esteso il
> `DataSeeder` e configurato `.gitignore`. Dopo un **merge** e un successivo **revert** del merge
> `provaDBMS`, lo stato è stato verificato: nessun conflitto residuo, refactor intatto, codice
> coerente. La **gerarchia `ROLE_ADMIN > ROLE_COACH` è stata rimossa di proposito** (vedi 11).
> Il blocco "gestione partite" è stato tolto dal revert e va rifatto (sezione 12). Nella stessa
> giornata sono state fissate alcune **convenzioni di progettazione** trasversali (sezione 13).

> **Nota (21/10/2026):** avviato il **prototipo di design dell'area Coach** (sezione 14): mockup
> HTML cliccabile e **separato dal codice**, con dati finti ma permessi reali, per definire il
> design dell'applicazione da presentare al professore prima di scriverlo nei template Thymeleaf.

> **Nota (22/10/2026):** **deciso il disegno del modulo partite + scouting** (sezione 12 riscritta):
> l'admin crea partita e set in anticipo, il coach scouta solo la propria squadra accumulando i
> contatori lato interfaccia e a "Fine set" crea in blocco le `Performance` del set; i punteggi
> restano all'admin e non influiscono sulle statistiche. **Nessun cambio di schema.** Evoluto anche
> il prototipo (sezione 14): ruolo Ospite + consultazione pubblica, statistiche con grafici a torta
> dei 5 fondamentali, accenti arancioni stile Mikasa.

---

## 1. Correzioni che impedivano la COMPILAZIONE

### 1.1 `model/Player.java` — metodo `setPassword`
- **Prima:** `public String setPassword(...)` con tipo di ritorno ma senza `return` → `missing return statement`.
- **Dopo:** `public void setPassword(...)`. Un setter non restituisce valori.

---

## 2. Correzioni che impedivano l'AVVIO

### 2.1 `model/Match.java` — collezione dei set senza annotazione
- **Prima:** `private List<Set> sets` senza annotazione JPA → `MappingException`.
- **Dopo:** `@OneToMany(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true)`.
- **Perché:** uno-a-molti con lato proprietario `GameSet` (`@ManyToOne @JoinColumn("match_id")`);
  `cascade`/`orphanRemoval` riflettono la composizione.

### 2.2 `VolleyballScoutingApplication.java` — rotta duplicata su `/`
Classe main `@RestController` con `@GetMapping("/")` → `Ambiguous mapping`. Ripulita: solo
`@SpringBootApplication` + `main`.

### 2.3 (Operativo) Classi compilate obsolete in `target/`
`ClassNotFoundException` da vecchie classi → risolto con `./mvnw clean`.

---

## 3. Correzioni LOGICHE / di configurazione

### 3.1 `pom.xml` — versione di Thymeleaf
Rimossa la `<version>` forzata `2.1.8.RELEASE`; aggiunta `thymeleaf-extras-springsecurity6`.

### 3.2 `model/Coach.java` — `getPassword()` restituiva stringa vuota
Da `return "";` (BCrypt non autenticava mai) a `return password;` + `setPassword(String)`.

### 3.3 `dto/PlayerFormDto.java` — validazione ruolo e data
`@NotBlank` su enum → `@NotNull`; `@DateTimeFormat` su `birthdate`.

### 3.4 `config/SecurityConfig.java` — regole di accesso (stato 05/10/2026)
- **Pubbliche:** `/`, `/login`, `/home`, risorse statiche; in sola lettura (GET) `/teams`,
  `/teams/list`, `/teams/details/**`, `/players/list`, `/players/details/**`, `/players/free`,
  `/matches`, `/matches/list`, `/matches/details/**`.
- **ADMIN:** `/admin/**`; partite `/matches/new`, `/matches/create`, `/matches/*/edit`,
  `/matches/*/update`, `/matches/*/score/**`, `/matches/*/sets/**`; **console H2** `/h2-console/**`.
- **COACH:** `/players/new`, `/players/create`, `/players/*/edit`, `/players/*/update`;
  `/teams/new`, `/teams/create`, `/teams/*/edit`, `/teams/*/update`; `/matches/*/scout/**`.
- `anyRequest().authenticated()`; `defaultSuccessUrl("/home", true)`; `frameOptions(sameOrigin)` e
  `csrf.ignoringRequestMatchers("/h2-console/**")`.
- **Nessuna gerarchia dei ruoli** (rimossa di proposito, vedi 11).
- *Pulizia consigliata:* restano due `import` inutilizzati (`RoleHierarchy`, `RoleHierarchyImpl`) e un
  commento fuorviante sopra `/h2-console` ("ripristinato a permitAll").
- **Mancano ancora** i `permitAll` per `/coaches/new` e `/coaches/register` (vedi 10.9).

### 3.5 `controller/PlayerWebController.java`
- Redirect corretti (`/players/list`, `/players/details/{id}`); `formAction` e `roles` nelle liste.
- **Guardia anti-NPE (05/10):** se `coachService.getCoachTeam(...)` è `null`, l'errore viene esposto
  come errore globale via `BindingResult` invece di una `NullPointerException` su `t.getId()`.
- **Da ripulire:** iniettava `CoachRepository`/`PlayerRepository` per il controllo di username
  duplicato — violazione di stratificazione, da sostituire con `UserAccountService` (vedi 13.3/13.5).

### 3.6 `controller/TeamWebController.java`
- `editTeam` → `/teams/details/{id}`; `formAction`.
- **Vincolo 1:1 e iniezione (05/10):** iniettato `CoachService`, corretta l'invocazione da statica a
  d'istanza; se il coach ha già una squadra → redirect al dettaglio (e in `create` errore dedicato).

### 3.7 Collegamento Coach↔Team alla creazione squadra — **APPLICATO**
`TeamService.save(TeamFormDto, UUID coachId)` carica il coach gestito e imposta `coach.setTeam(saved)`;
il controller usa `@AuthenticationPrincipal AppUserDetails` e passa `userDetails.getId()`.

### 3.8 `application.properties` — `spring.jpa.open-in-view=true` — **APPLICATO**

---

## 4. NUOVI file Java / configurazione
- `controller/HomeController.java` — `GET /`, `/login`, `/home`.
- `model/Admin.java` + `repository/AdminRepository.java` (vedi 10.7).
- `repository/AddressRepository.java` — **rimosso** (Address embeddable).
- `config/DataSeeder.java` — solo sviluppo/test: admin da properties, un coach demo con squadra e 2
  giocatori, e **(05/10) un secondo coach demo** (serie C Polisportiva Remo Masi, Rufina) per testare
  scenari a due squadre. Demo: `coach`/`coach123`, giocatori `player123`, admin `admin`/`admin123`.
- **`.gitignore` (05/10):** esclusi `Database/*.db` e `*.trace.db`.

---

## 5. Template Thymeleaf
`fragments/layout.html`, `index.html`, `login.html`, `home.html`,
`players/{list,form,detail,NoTeamList}.html`, `teams/{list,form,detail}.html`, `static/css/style.css`.
- **Refactor `players/form.html` (05/10):** `th:field="*{...}"` al posto dei `th:value` manuali
  (binding bidirezionale, select enum, messaggi di Bean Validation); rimosso `#dates.format(...)`
  (supporta solo `java.util.Date`, su `LocalDate` dava `TemplateProcessingException`); aggiunti errori
  globali (`#fields.hasGlobalErrors()`) e `readonly` su `username` in modifica.
- *Mancano:* `templates/coaches/` (10.9) e i template partite/scouting (12).

---

## 6. Operazioni sul DATABASE
- Ogni cambio di schema (id → UUID, Address embeddable, doppia FK → singola, 13 contatori in
  `Performance`) richiede di **rigenerare** il DB cancellando `Database/VolleyDB.mv.db` e `.trace.db`.
- **Episodio UUID→BIGINT (14/09):** `Feature not supported: "UUID"` e
  `Data conversion error "BIGINT to UUID"` con `ddl-auto=update` su DB vecchio.

---

## 7. Problemi noti risolti
- **Filtro per ruolo** (`findByRole`): conversione con `PlayerRole.valueOf(...)`.
- **Rinomina `Set`→`GameSet`:** corrette le JPQL (`p.set.match` → `p.gameSet.match`), il metodo derivato
  (`findByPlayerIdAndSetId` → `findByPlayerIdAndGameSetId`) e i path Criteria (`root.get("set")` →
  `root.get("gameSet")`). **Lezione:** rinominare un campo di un'entità rompe le query JPQL e i path
  Criteria, che usano il **nome dell'attributo** come stringa e non il getter.

---

## 8. Come avviare e testare
1. (se lo schema è cambiato) cancellare `Database/VolleyDB.mv.db` e `.trace.db`.
2. `./mvnw clean spring-boot:run`
3. `http://localhost:8080` → coach **`coach`/`coach123`**, admin **`admin`/`admin123`**.
4. Console H2 su `/h2-console` — **riservata all'admin**.

---

## 9. Fotografia dello stato

### 9.1 Architettura attuale
Spring MVC a livelli: **Controller → Service → Repository → Entity**, viste **Thymeleaf**.
- **`model/`** — `Address` (embeddable), `Team`, `Coach`, `Player`, `Admin`, `Match`, `GameSet`,
  `Performance`(+`PerformanceId`), `Referee`, enum `PlayerRole`; id `UUID` da `BaseModel`.
- **`repository/`** — Spring Data JPA + `PerformanceSpecifications`.
- **`service/`** — `PlayerService`, `TeamService`, `CoachService`, `PerformanceService`,
  `MatchService` (query di lettura; `save` da completare, vedi 12), `RefereeService`,
  `UserAccountService` (vedi 13.5).
- **`security/`** — `AppUserDetails`, `AppUserDetailsService`, `config/SecurityConfig`
  (nessuna gerarchia ruoli).
- **`dto/`** — form DTO (+ `MatchFormDTO`) + `PlayerStatsDTO`/`TeamStatsDTO` + `UserAccount`;
  `validation/OnCreate`,`OnUpdate`.
- **`controller/`** — `HomeController`, `PlayerWebController`, `TeamWebController`,
  `CoachWebController` (incompleto). **Manca** `MatchWebController` (vedi 12).
- **`query/`** — `PerformanceQuery` (Builder → Specification).

### 9.2 Prossimi passi
- **A.** Registrazione allenatore (10.9) — applicando anche le convenzioni della sezione 13.
- **B.** Gestione partite + **scouting** (sezione 12): completare `MatchService.save`, aggiungere
  `MatchWebController` (admin) e la pagina/endpoint di scouting del coach.
- **C.** Report/analisi: i calcoli ci sono, servono i dati.
- **D.** Rifiniture: cambio password giocatore; test unitari.

---

## 10. Revisione domain model e scelte progettuali — ricevimento professore

### 10.1 `BaseModel` come classe base — **DECISO: mantenere**
`@MappedSuperclass` (id UUID + `createdAt`), con nota "tutte ereditano TRANNE `Performance`/
`PerformanceId`". Legame = generalizzazione, non associazione.

### 10.2 Getter/setter — **DECISO: mantenere** (il tema è il *dove* sta la logica, vedi 10.4).

### 10.3 `Address` come `@Embeddable` — **APPLICATO**
Non estende `BaseModel`; `Team`/`Match` con `@Embedded`; `AddressRepository` rimosso.
*Residui:* `Match` riusa l'istanza di `teamHome.getAddress()`; consigliati `equals`/`hashCode`.
*Nota runtime:* un embedded con tutte le colonne null torna `null` (gestire con `th:if`).

### 10.4 Comportamenti di dominio — **APPLICATO (parziale)**
`Team.assignCoach`/`Coach.assignTeam` (setter semplici, sincronizzazione solo negli `assign`).
Da portare nel model: `Player.joinTeam/leaveTeam` e `Match.recountSetsWon()`.

### 10.5 Proprietario `@OneToOne` Coach↔Team — **APPLICATO**
Coach proprietario (`@JoinColumn("team_id")`), `Team.coach` con `mappedBy="team"`.

### 10.6 Affiliazione giocatori — **DECISO**
`Team` non conosce i giocatori; affiliazione unidirezionale su `Player.team` (`null` = svincolato).

### 10.7 Utente amministratore — **APPLICATO**
`Admin` + `AdminRepository`; risoluzione username a cascata Admin → Coach → Player; credenziali da
`application.properties` nel `DataSeeder` (BCrypt). Seed all'avvio, non console H2; Spring Boot Admin
di codecentric non pertinente (monitoraggio, non gestione dati).

### 10.8 Diagrammi — **INDICAZIONI**
Repository e controller non vanno nel domain model; servono un diagramma a package/strati e dei
sequence diagram. `Admin` resta senza associazioni (relazione operativa, non strutturale).

### 10.9 Registrazione allenatore — **UNICO PUNTO ANAGRAFICA APERTO**
Manca `templates/coaches/`; `CoachService.save` è un TODO; il POST `/coaches/register` termina con
`return "no";`; un ramo d'errore rimanda a `players/form`; mancano i `permitAll` per `/coaches/new` e
`/coaches/register`. **Trappola nota:** il costruttore di `Coach` **non** imposta `birthdate`, che è
`@Column(nullable = false)` → va settato esplicitamente dal form, altrimenti violazione NOT NULL.
Quando si scriverà `CoachService.save` vanno applicate le convenzioni della sezione 13 (firma a id dove
serve, cifratura BCrypt della password scelta dall'utente, guardia `isUsernameTaken`).

### 10.10 Uso dell'IA e motivazione delle scelte — **DA FARE (richiesto dal prof)**
(a) log delle interazioni con l'IA; (b) sezione "scelte progettuali" che motivi `UUID` vs `Long`,
`@MappedSuperclass`, `@Enumerated(STRING)`, `@EmbeddedId`, `cascade`/`orphanRemoval`, i `fetch`, le
`@UniqueConstraint`, `Address` embeddable — e ora anche le convenzioni della sezione 13.

### 10.11 Bug di avvio `birthdate` — **RISOLTO**
`coach.setBirthdate(LocalDate.EPOCH)` nel `DataSeeder`.

---

## 11. Architettura "Soluzione A" — separazione Admin / Coach (05/10/2026)

- **Admin:** gestisce l'**accesso dei coach** e l'infrastruttura; in prospettiva calendario partite,
  squadre di campionato e **referto ufficiale dei set**; **console H2** riservata a lui.
- **Coach:** tutto ciò che riguarda la **propria squadra** — anagrafica giocatori e (in arrivo) il
  **rilevamento live delle azioni/`Performance`**.

**Motivo:** evitare conflitti di scrittura concorrente su H2. Se due coach scoutassero la stessa
partita rischierebbero di sovrascrivere i punteggi condivisi del `GameSet`; tenendo il referto in capo
all'admin e le sole azioni individuali in capo al coach, le scritture restano separate.

**Gerarchia ruoli rimossa (voluta):** Admin e Coach hanno permessi **disgiunti**. Effetto positivo: le
rotte `hasRole("COACH")` sono raggiungibili solo dai coach, quindi `CoachService.getCoachTeam(id)` (che
**lancia eccezione** per id non-coach) riceve sempre un id valido → niente `500`.
**Attenzione futura:** reintroducendo la gerarchia servirebbe una guardia su `getCoachTeam`.

---

## 12. Modulo gestione partite + scouting — DISEGNO DECISO (22/10/2026)

**Stato del codice (22/10):** esistono `service/MatchService` (molte query di lettura), `dto/MatchFormDTO`,
entità `Referee` + `service/RefereeService`, `repository/MatchRepository`. **`MatchService.save(MatchFormDTO)`
è ancora un `//TODO` e così com'è NON COMPILA** (manca il corpo del metodo). **Mancano** `MatchWebController`,
la pagina di scouting e i template `matches/`.

### 12.1 Principio dello scouting
L'obiettivo del progetto sono le **statistiche di giocatori e squadre**; la partita è "sfondo". Quindi:
- **niente** log azione-per-azione, **niente** salvataggio di formazioni, rotazioni o sostituzioni;
- dato **aggregato per set**: una `Performance` per *(giocatore, GameSet)* con i contatori già esistenti;
- **nessuna modifica di schema** — si usano `Match`, `GameSet`, `Performance` così come sono.

### 12.2 Divisione dei ruoli (coerente con la Soluzione A, sez. 11)
- **Admin:** crea la partita **e i suoi set** PRIMA dell'inizio; a fine partita inserisce i **punteggi**
  aggiornando i `GameSet` già creati. Possiede il ciclo di vita del set e il referto.
- **Coach:** durante la partita **scouta solo la propria squadra**; a "Fine set" salva le `Performance`.
  **Non** tocca i punteggi.
- Il **punteggio del set non entra nel calcolo delle statistiche** (nascono dai contatori di `Performance`),
  quindi l'admin può inserirlo con comodo a fine partita senza influire sulla rappresentazione dei dati.

### 12.3 Flusso
1. L'admin crea `Match` + **5 `GameSet` segnaposto a 0-0** (5 = massimo; i set non giocati restano vuoti,
   senza `Performance`).
2. Il coach apre la pagina di scouting, che tiene **in memoria lato frontend** i contatori per ogni suo
   giocatore (nessuna scrittura durante il set).
3. Al **"Fine set"**: **una sola** POST col batch `{giocatore, contatori}` → il server crea una
   `Performance(giocatore, GameSet corrente)` **solo per i giocatori con almeno un'azione**, collegandola a
   giocatore e set. Poi si passa al `GameSet` successivo.
4. A fine partita l'admin inserisce i punteggi dei set effettivamente giocati.

**Perché i set si creano in anticipo:** la `Performance` ha chiave composta *(player, GameSet)* e richiede un
`GameSet` già persistito; creando i set prima, il coach trova sempre il set a cui agganciarsi (niente "set non
trovato"), e l'admin inserisce i punteggi quando vuole.

### 12.4 Da sviluppare (backend)
- **`MatchService.save(MatchFormDTO)`** — completare: crea la `Match` (home, guest, referee, `matchDateTime`,
  `address` ereditato dalla squadra di casa) **e i 5 `GameSet` a 0-0**.
- **`MatchWebController` (ADMIN)** — `/matches/new`, `/matches/create`, elenco, dettaglio, + endpoint per
  **inserire i punteggi** a fine partita (aggiorna `teamHomePoints/teamGuestPoints` dei `GameSet` già creati).
  Rotte già riservate ad ADMIN (sez. 3.4).
- **Scouting coach:**
  - `GET /matches/{id}/scout` → pagina scouting. **Guardia:** la squadra del coach deve giocare quella
    partita; espone solo i propri giocatori.
  - `POST` "Fine set" (es. `/matches/{id}/sets/{setNumber}/scout`) → riceve il batch e **crea/aggiorna** le
    `Performance` di quel `GameSet`.
  - Rotta `/matches/*/scout/**` già riservata a COACH (sez. 3.4).
- **Template** — `matches/{list,detail,form}.html` (admin) + pagina scouting (coach).

### 12.5 Guardie / accorgimenti
- **"Fine set" idempotente:** ripremere non deve duplicare — grazie alla PK composta *(player, GameSet)* un
  secondo salvataggio **aggiorna** la stessa riga (`save`/merge).
- **Solo chi ha fatto azioni** finisce in `Performance`; i "set giocati" si deducono dalle `Performance`
  esistenti.
- **Scritture disgiunte:** il coach scrive solo righe `Performance` dei propri giocatori, l'admin solo i
  punteggi dei `GameSet` → nessun conflitto concorrente (è il motivo della Soluzione A, sez. 11).
- **DTO del batch "Fine set":** lista per giocatore con `playerId`, `servePoint/serveInPlay/serveError`,
  `attackPoint/attackInPlay/attackError`, `blockPoint/blockInPlay/blockError`, `receiveGood/receiveError`,
  `digGood/digError`; il server risolve il `GameSet` da `{matchId, setNumber}`.

### 12.6 Fuori scope (per chiarezza)
Niente entità Lineup/Rotation/Substitution; niente log evento-per-evento; niente scouting degli avversari.
Il "campo" con posizioni/panchina resta **solo ergonomia dell'interfaccia** per registrare in fretta
(lo sketch verrà implementato quando il backend partite sarà pronto).

---

## 13. Convenzioni di progettazione consolidate (05/10/2026)

Regole trasversali fissate discutendo il codice; sono tutte **motivabili in relazione** (richiesta 10.10).

### 13.1 Ordinamento delle query
`findAll()` **non garantisce alcun ordine**: genera un `select` senza `ORDER BY`, quindi l'ordine lo
decide il DB (e con id `UUID` casuali sembrerebbe comunque arbitrario). Dove l'ordine conta:
- **`Sort` passato dal service** quando l'ordine è una scelta del caso d'uso (es. squadre per nome);
- **metodo derivato con `OrderBy`** quando l'ordine è *il* modo canonico di leggere quei dati
  (es. `findAllByOrderByMatchDateTimeDesc`);
- **`@OrderBy("setNumber ASC")`** sulla collezione `Match.gameSets`, così `match.getSets()` torna i set
  in ordine (serve per il referto e per `recountSetsWon()`).
Sconsigliato ridichiarare `findAll()` con `@Query`: cambia il significato di un metodo ereditato.

### 13.2 Id vs entità ai confini dei service — **convenzione adottata**
- **Confine pubblico del service (chiamato dai controller): si passano gli id.** Il controller ha solo
  id (URL, principal); il service apre la transazione e carica l'entità **gestita**.
- **Dentro il service, o tra service e metodi interni: si passano le entità**, per non ricaricare.
**Motivo concreto:** passare un'entità caricata nel controller funziona oggi **solo grazie a
`open-in-view=true`** (il persistence context resta aperto per tutta la richiesta); con
`open-in-view=false` arriverebbe *detached*. Appoggiare la correttezza a una property è fragile.
**Applicato:** `PlayerService.save(PlayerFormDTO, UUID teamId)` e `addPlayerToTeam(UUID, UUID)`
(prima aveva una firma mista `UUID playerId` + `Team`), con `TeamService` iniettato in `PlayerService`
(nessun ciclo: `PlayerService → TeamService → CoachService → repository`).
**Proposto anche per `MatchRepository`:** metodi per squadra che accettano `UUID teamId` confrontando
`m.teamHome.id = :teamId` (non basta rinominare il parametro: cambia il **path** nella JPQL), più
spazi espliciti nelle stringhe concatenate (`"...WHERE" + "(m..."` produce `WHERE(`) e `ORDER BY`
espliciti. Nota: con `@Query` il **nome del metodo non viene interpretato**, quindi i rename sono solo
leggibilità; e `@Repository` su un'interfaccia Spring Data è ridondante.

### 13.3 Service vs repository fra aggregati — **convenzione adottata**
Un service usa **il proprio repository** direttamente, e gli **altri aggregati tramite il loro service**
(è lì che stanno "non trovato", regole e logiche derivate: usando il repository altrui le duplichi o le
salti). **Eccezione:** i **cicli fra bean** — i repository non dipendono da nulla, quindi quando due
aggregati devono conoscersi a vicenda uno dei due usa il repository dell'altro.
**Violazione trovata e da correggere:** `PlayerWebController` iniettava `CoachRepository` e
`PlayerRepository` per il controllo di username duplicato — un controller che parla con la persistenza,
saltando il service (rompe il diagramma a strati).

### 13.4 Invarianti difese nel service, messaggi nel controller
Il controllo va **duplicato di proposito**: il controller lo esegue per dare un errore gentile nel form
(`BindingResult`), il service lo ripete per garantire l'invariante a chiunque lo chiami (anche fuori dal
flusso web). L'eccezione del service è un **backstop difensivo** che nel flusso normale non scatta.
**Applicato in `PlayerService.save`:** guardia sul numero di maglia già assegnato in squadra.
**Accorgimento:** le guardie vanno **prima** di `passwordEncoder.encode(...)`, che è volutamente lento
(BCrypt): altrimenti si spreca lavoro su una chiamata destinata a fallire.

### 13.5 Unicità globale dello username — `UserAccountService` (proposto)
Lo username deve essere unico **fra Admin, Coach e Player**: il vincolo `unique` del DB copre solo la
singola tabella, e una collisione fra tipi diversi renderebbe un utente **non più autenticabile**
(la risoluzione è a cascata e lo "oscurerebbe"). Per non duplicare la cascata né appesantire
`AppUserDetailsService` — che è un **adattatore di Spring Security**, non un service di dominio — la
cascata viene estratta una volta sola:
- `service/UserAccount` (record: `id`, `username`, `password`, `role`);
- `service/UserAccountService` con `findByUsername(String)` (cascata **Admin → Coach → Player**, ordine
  che è esso stesso una regola di dominio) e `isUsernameTaken(String)`;
- `AppUserDetailsService` ridotto a mappare `UserAccount` → `AppUserDetails`;
- nei tre repository un `findByUsernameIgnoreCase(String)`.
**Direzione delle dipendenze risultante:** `security → service → repository` (prima sarebbe stata
`service → security`). **Effetto da conoscere:** usando `IgnoreCase`, **unicità e login condividono la
stessa nozione di uguaglianza** e il login diventa case-insensitive (alternativa: normalizzare lo
username in minuscolo al salvataggio).
**Da agganciare in:** `PlayerService.save`, `PlayerWebController` (al posto dei repository) e la futura
`CoachService.save`.

### 13.6 Stile: niente lambda nel codice nuovo
Scelta del gruppo: `Optional` gestito con `isPresent()`/`isEmpty()` + `get()` invece di
`map(...)`/`orElseThrow(() -> ...)`. Coerente con lo stile originale di `AppUserDetailsService`.
*Nota:* `orElseThrow(() -> new IllegalArgumentException(...))` resta in `findById` di `CoachService`,
`TeamService` e `PlayerService`: da decidere se uniformare anche quelli.

---

## 14. Prototipo di design — area Coach (21/10/2026, agg. 22/10)

- **Cos'è:** prototipo **HTML cliccabile** pubblicato come Artifact privato su claude.ai,
  **separato dal codice del progetto** (dati finti, nessun Thymeleaf né backend). Serve a decidere il
  design dell'applicazione da mostrare al professore, prima di scriverlo nei template reali.
- **URL:** https://claude.ai/artifact/U5k8cYJHTSjwJmVtnLExMX (privato: va condiviso dal menu *Share*
  della pagina per farlo vedere ad altri).
- **Base di design:** **colori del sito attuale** (blu royal su grigio chiaro, da `static/css/style.css`),
  tipografia **Barlow**; **accenti arancioni in stile palla Mikasa** (logo-pallone + dettagli).
- **Ruoli e permessi (consultazione pubblica):** ruolo **Ospite** (non registrato) accanto all'**Allenatore**.
  Squadre, rose e **statistiche sono pubbliche per tutti**; la **modifica** è riservata al coach e **solo per la
  propria squadra**. Dimostrati dal vivo con un interruttore demo (Ospite/Allenatore, con/senza squadra):
  "Crea squadra" sparisce a squadra assegnata, azioni bloccate senza squadra, sola lettura sulle squadre altrui.
- **Statistiche con grafici a torta:** per ogni giocatore i **5 fondamentali** — Battuta, Ricezione, Attacco,
  Muro, Difesa — con profondità **3** (punto/in campo/errore) per Attacco/Battuta/Muro e **2** (buono/errore)
  per Ricezione/Difesa, **allineata a `Performance`** (`attack/serve/blockPoint·InPlay·Error`,
  `receiveGood/Error`, `digGood/Error`). Torta con efficacia % al centro e dati puri + % accanto. Nelle
  statistiche **di squadra** compaiono sempre **tutti e 5** (anche vuoti).
- **Schermate:** dashboard azioni; scheda squadra (rosa + statistiche, pubblica); scheda statistiche giocatore;
  crea/modifica squadra e giocatore; cerca giocatori (tutte le squadre) e cerca squadre.
- **Interfaccia di scouting (campo):** sketch acquisito ma **non ancora prototipato**; verrà fatto quando il
  backend partite sarà pronto (vedi 12). Disegno dati concordato: variabili per giocatore accumulate lato UI,
  create in `Performance` a "Fine set".
- **Prossimi passi:** feedback sul design; eventuale estensione al Giocatore; poi **porting nei template
  Thymeleaf veri** (permessi via `sec:authorize` + flag "ha squadra").

---

> **Metodo di lavoro concordato:** le modifiche al backend vengono **proposte e discusse** qui e poi
> **applicate dal gruppo** dopo verifica; questo registro tiene traccia di *deciso / applicato / da fare*.
