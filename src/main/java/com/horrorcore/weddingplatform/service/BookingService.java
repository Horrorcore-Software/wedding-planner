package com.horrorcore.weddingplatform.service;

import com.horrorcore.weddingplatform.exception.VendorException;
import com.horrorcore.weddingplatform.model.*;
import com.horrorcore.weddingplatform.repository.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final VendorRepository vendorRepository;
    private final VenueRepository venueRepository;

    public BookingService(BookingRepository bookingRepository, VendorRepository vendorRepository, VenueRepository venueRepository) {
        this.bookingRepository = bookingRepository;
        this.vendorRepository = vendorRepository;
        this.venueRepository = venueRepository;
    }

    @Transactional
    public Booking createBooking(@Valid Booking booking, Long vendorId) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new VendorException("Vendor not found", HttpStatus.NOT_FOUND));

        if (!vendor.isVerified()) {
            throw new VendorException("Cannot book unverified vendor", HttpStatus.BAD_REQUEST);
        }

        // Check vendor availability
        if (isVendorBooked(vendor, booking.getEventDate())) {
            throw new VendorException("Vendor is already booked for this date", HttpStatus.CONFLICT);
        }

        // Validate booking date
        if (booking.getEventDate().isBefore(LocalDateTime.now())) {
            throw new VendorException("Cannot book for past dates", HttpStatus.BAD_REQUEST);
        }

        booking.setVendor(vendor);
        booking.setBookingDate(LocalDateTime.now());
        booking.setStatus(BookingStatus.PENDING);

        validateBookingDetails(booking);

        return bookingRepository.save(booking);
    }

    private boolean isVendorBooked(Vendor vendor, LocalDateTime eventDate) {
        // Check if vendor has any confirmed bookings for the given date
        return bookingRepository.existsByVendorAndEventDateAndStatus(
                vendor, eventDate, BookingStatus.CONFIRMED);
    }

    private void validateBookingDetails(Booking booking) {
        // Validate advance payment
        if (booking.getAdvancePayment().compareTo(booking.getTotalAmount()) > 0) {
            throw new VendorException(
                    "Advance payment cannot be greater than total amount",
                    HttpStatus.BAD_REQUEST
            );
        }

        // Validate number of guests based on vendor type
        if (booking.getVendor().getBusinessType() == VendorType.VENUE) {
            VenueDetails venue = venueRepository.findByVendor(booking.getVendor())
                    .orElseThrow(() -> new VendorException("Venue details not found", HttpStatus.NOT_FOUND));

            if (booking.getNumberOfGuests() > venue.getMaxCapacity()) {
                throw new VendorException(
                        "Number of guests exceeds venue capacity",
                        HttpStatus.BAD_REQUEST
                );
            }
        }
    }

    @Transactional
    public Booking confirmBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new VendorException("Booking not found", HttpStatus.NOT_FOUND));

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new VendorException(
                    "Cannot confirm booking with status: " + booking.getStatus(),
                    HttpStatus.BAD_REQUEST
            );
        }

        booking.setStatus(BookingStatus.CONFIRMED);
        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new VendorException("Booking not found", HttpStatus.NOT_FOUND));

        if (booking.getStatus() != BookingStatus.PENDING &&
                booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new VendorException(
                    "Cannot cancel booking with status: " + booking.getStatus(),
                    HttpStatus.BAD_REQUEST
            );
        }

        booking.setStatus(BookingStatus.CANCELLED);
        return bookingRepository.save(booking);
    }

    public List<Booking> getVendorBookings(Long vendorId) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new VendorException("Vendor not found", HttpStatus.NOT_FOUND));

        return bookingRepository.findByVendorOrderByEventDateDesc(vendor);
    }
}
