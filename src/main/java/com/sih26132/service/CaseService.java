package com.sih26132.service;

import com.sih26132.dto.cases.CaseCreateRequest;
import com.sih26132.dto.cases.CaseResponse;
import com.sih26132.entity.Case;
import com.sih26132.entity.CropCycle;
import com.sih26132.entity.Farm;
import com.sih26132.entity.User;
import com.sih26132.repository.CaseRepository;
import com.sih26132.repository.CropCycleRepository;
import com.sih26132.repository.FarmRepository;
import com.sih26132.repository.UserRepository;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.Year;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CaseService {

    private final CaseRepository caseRepository;
    private final CropCycleRepository cropCycleRepository;
    private final FarmRepository farmRepository;
    private final UserRepository userRepository;

    public CaseService(
            CaseRepository caseRepository,
            CropCycleRepository cropCycleRepository,
            FarmRepository farmRepository,
            UserRepository userRepository) {

        this.caseRepository = caseRepository;
        this.cropCycleRepository = cropCycleRepository;
        this.farmRepository = farmRepository;
        this.userRepository = userRepository;
    }

    public CaseResponse createCase(
            CaseCreateRequest request,
            String identifier) {

        User user = findUser(identifier);

        CropCycle cropCycle = cropCycleRepository
                .findById(request.getCropCycleId())
                .orElseThrow(() ->
                        new RuntimeException("Crop cycle not found")
                );

        verifyCropCycleOwnership(cropCycle, user);

        String caseNumber = generateCaseNumber();

        Case newCase = Case.builder()
                .caseNumber(caseNumber)
                .cropCycle(cropCycle)
                .createdByUser(user)
                .status(
                        request.getStatus() != null
                                ? request.getStatus()
                                : "NEW"
                )
                .description(request.getDescription())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();

        Case savedCase = caseRepository.save(newCase);

        return mapToResponse(savedCase);
    }

    @Transactional(readOnly = true)
    public List<CaseResponse> getMyCases(
            String identifier) {

        User user = findUser(identifier);

        return caseRepository
                .findByCreatedByUser(user)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CaseResponse getMyCase(
            UUID caseId,
            String identifier) {

        User user = findUser(identifier);

        Case caseEntity = caseRepository
                .findByIdAndCreatedByUser(caseId, user)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Case not found or access denied"
                        ));

        return mapToResponse(caseEntity);
    }

    public CaseResponse updateCase(
            UUID caseId,
            CaseCreateRequest request,
            String identifier) {

        User user = findUser(identifier);

        Case caseEntity = caseRepository
                .findByIdAndCreatedByUser(caseId, user)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Case not found or access denied"
                        ));

        if (request.getCropCycleId() != null) {

            CropCycle cropCycle = cropCycleRepository
                    .findById(request.getCropCycleId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Crop cycle not found"
                            ));

            verifyCropCycleOwnership(cropCycle, user);

            caseEntity.setCropCycle(cropCycle);
        }

        if (request.getStatus() != null) {
            caseEntity.setStatus(request.getStatus());
        }

        caseEntity.setDescription(request.getDescription());
        caseEntity.setLatitude(request.getLatitude());
        caseEntity.setLongitude(request.getLongitude());
        caseEntity.setUpdatedAt(OffsetDateTime.now());

        Case updatedCase = caseRepository.save(caseEntity);

        return mapToResponse(updatedCase);
    }

    public void deleteCase(
            UUID caseId,
            String identifier) {

        User user = findUser(identifier);

        Case caseEntity = caseRepository
                .findByIdAndCreatedByUser(caseId, user)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Case not found or access denied"
                        ));

        caseRepository.delete(caseEntity);
    }

    @Transactional(readOnly = true)
    public List<CaseResponse> getCasesByStatus(
            String status,
            String identifier) {

        User user = findUser(identifier);

        return caseRepository
                .findByCreatedByUser(user)
                .stream()
                .filter(caseEntity ->
                        caseEntity.getStatus()
                                .equalsIgnoreCase(status))
                .map(this::mapToResponse)
                .toList();
    }

    private User findUser(String identifier) {

        return userRepository.findByEmail(identifier)
                .or(() -> userRepository.findByPhone(identifier))
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found: " + identifier
                        ));
    }

    private void verifyCropCycleOwnership(
            CropCycle cropCycle,
            User user) {

        Farm farm = cropCycle.getFarm();

        if (!farm.getUser().getId().equals(user.getId())) {
            throw new RuntimeException(
                    "Crop cycle does not belong to the current user"
            );
        }
    }

    private String generateCaseNumber() {

        int year = Year.now().getValue();

        String caseNumber;

        do {
            long number = System.currentTimeMillis() % 1_000_000;

            caseNumber = String.format(
                    "CASE-%d-%06d",
                    year,
                    number
            );

        } while (caseRepository.existsByCaseNumber(caseNumber));

        return caseNumber;
    }

    private CaseResponse mapToResponse(Case caseEntity) {

        return CaseResponse.builder()
                .id(caseEntity.getId())
                .caseNumber(caseEntity.getCaseNumber())
                .cropCycleId(
                        caseEntity.getCropCycle().getId()
                )
                .createdByUserId(
                        caseEntity.getCreatedByUser().getId()
                )
                .assignedToUserId(
                        caseEntity.getAssignedToUser() != null
                                ? caseEntity.getAssignedToUser().getId()
                                : null
                )
                .status(caseEntity.getStatus())
                .description(caseEntity.getDescription())
                .latitude(caseEntity.getLatitude())
                .longitude(caseEntity.getLongitude())
                .createdAt(caseEntity.getCreatedAt())
                .updatedAt(caseEntity.getUpdatedAt())
                .build();
    }
}