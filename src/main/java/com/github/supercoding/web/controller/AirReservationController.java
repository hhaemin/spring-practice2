package com.github.supercoding.web.controller;

import com.github.supercoding.repository.reservatios.Reservation;
import com.github.supercoding.service.AirReservationService;
import com.github.supercoding.service.exceptions.InvalidValueException;
import com.github.supercoding.service.exceptions.NotAcceptException;
import com.github.supercoding.service.exceptions.NotFoundException;
import com.github.supercoding.web.dto.airline.ReservationRequest;
import com.github.supercoding.web.dto.airline.ReservationResult;
import com.github.supercoding.web.dto.airline.Ticket;
import com.github.supercoding.web.dto.airline.TicketResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity findAirlineTickets(
            @Parameter(name = "user-Id", description = "유저 ID", example = "1") @RequestParam("userId") Integer userId,
            @Parameter(name = "airline-ticket-type", description = "항공권 타입", example = "왕복|편도")@RequestParam("airlineTicketType") String ticketType) {
        try {
            List<Ticket> tickets = airReservationService.findUserFavoritePlaceTickets(userId, ticketType);
            TicketResponse ticketResponse = new TicketResponse(tickets);
            return new ResponseEntity(ticketResponse, HttpStatus.OK);
        } catch (InvalidValueException ive){
            log.error("Client 요청에 문제가 있어 다음처럼 출력합니다. " + ive.getMessage());
            return new ResponseEntity(ive.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotFoundException nfe) {
            log.error("Client 요청이후 DB 검색 중 에러로 다음처럼 출력합니다. " + nfe.getMessage());
            return new ResponseEntity(nfe.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "User와 Ticket Id로 예약 진행")
    @PostMapping("/reservations")
    public ResponseEntity makeReservation(@RequestBody ReservationRequest reservationRequest){
        try{
            ReservationResult reservationResult = airReservationService.makeReservation(reservationRequest);
            return new ResponseEntity(reservationResult, HttpStatus.CREATED);
        } catch (NotFoundException nfe) {
            log.error("Client 요청이후 DB 검색 중 에러로 다음처럼 출력합니다. " + nfe.getMessage());
            return new ResponseEntity(nfe.getMessage(), HttpStatus.NOT_FOUND);
        } catch (NotAcceptException nae) {
            log.error("Client 요청이 모종의 이유로 거부됩니다." + nae.getMessage());
            return new ResponseEntity(nae.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
