package com.horrorcore.weddingplatform.repository;

import com.horrorcore.weddingplatform.model.Booking;
import com.horrorcore.weddingplatform.model.BookingStatus;
import com.horrorcore.weddingplatform.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    boolean existsByVendorAndEventDateAndStatus(
            Vendor vendor,
            LocalDateTime eventDate,
            BookingStatus status
    );

    List<Booking> findByVendorOrderByEventDateDesc(Vendor vendor);

    List<Booking> findByStatusAndEventDateBefore(
            BookingStatus status,
            LocalDateTime date
    );
}

