package com.horrorcore.weddingplatform.model;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "vendor_verification_documents")
public class VendorVerificationDocuments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String businessRegistrationNumber;
    private String taxIdentificationNumber;
    private String tradeLicenseNumber;
    private String identityProofUrl;
    private String bankAccountDetails;

    @OneToOne(mappedBy = "verificationDocuments")
    private Vendor vendor;
}