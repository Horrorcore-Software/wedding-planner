package com.horrorcore.weddingplatform.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
@Table(name = "vendors")
public class Vendor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String businessName;

    @Enumerated(EnumType.STRING)
    private VendorType businessType;

    @NotBlank
    private String registeredAddress;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String phone;

    private String website;

    @NotBlank
    private String ownerName;

    @Column(name = "is_verified")
    private boolean verified;

    @OneToOne(cascade = CascadeType.ALL)
    private VendorVerificationDocuments verificationDocuments;
}