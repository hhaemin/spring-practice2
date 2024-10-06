package com.github.supercoding.web.controller;

import com.github.supercoding.service.AirReservationService;
import com.github.supercoding.web.dto.airline.ReservationRequest;
import com.github.supercoding.web.dto.airline.ReservationResult;
import com.github.supercoding.web.dto.airline.Ticket;
import com.github.supercoding.web.dto.airline.TicketResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/air-reservation")
@Slf4j
public class AirReservationController {

    public final AirReservationService airReservationService;

    public AirReservationController(AirReservationService airReservationService) {
        this.airReservationService = airReservationService;
    }

    @Operation(summary = "선호하는 ticket 탐색")
    @GetMapping("/tickets")
    public TicketResponse findAirlineTickets(
            @Parameter(name = "user-Id", description = "유저 ID", example = "1") @RequestParam("userId") Integer userId,
            @Parameter(name = "airline-ticket-type", description = "항공권 타입", example = "왕복|편도")@RequestParam("airlineTicketType") String ticketType) {
            List<Ticket> tickets = airReservationService.findUserFavoritePlaceTickets(userId, ticketType);
            return new TicketResponse(tickets);
    }

    @Operation(summary = "User와 Ticket Id로 예약 진행")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/reservations")
    public ReservationResult makeReservation(@RequestBody ReservationRequest reservationRequest){
            return airReservationService.makeReservation(reservationRequest);

    }
}
