package com.horrorcore.weddingplatform.controller;


import com.horrorcore.weddingplatform.model.Booking;
import com.horrorcore.weddingplatform.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/vendors/{vendorId}")
    public ResponseEntity<Booking> createBooking(
            @PathVariable Long vendorId,
            @Valid @RequestBody Booking booking) {
        return ResponseEntity.ok(bookingService.createBooking(booking, vendorId));
    }

    @PostMapping("/{bookingId}/confirm")
    public ResponseEntity<Booking> confirmBooking(@PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingService.confirmBooking(bookingId));
    }

    @PostMapping("/{bookingId}/cancel")
    public ResponseEntity<Booking> cancelBooking(@PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingService.cancelBooking(bookingId));
    }

    @GetMapping("/vendors/{vendorId}")
    public ResponseEntity<List<Booking>> getVendorBookings(@PathVariable Long vendorId) {
        return ResponseEntity.ok(bookingService.getVendorBookings(vendorId));
    }
}