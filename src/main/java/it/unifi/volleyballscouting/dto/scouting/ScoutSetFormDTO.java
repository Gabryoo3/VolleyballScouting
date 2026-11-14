package it.unifi.volleyballscouting.dto.scouting;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ScoutSetFormDTO(@Valid @NotNull List<ScoutRowDTO> rows) {}
