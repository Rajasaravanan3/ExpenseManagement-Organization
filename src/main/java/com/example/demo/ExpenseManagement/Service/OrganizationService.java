package com.example.demo.ExpenseManagement.Service;

import org.dozer.DozerBeanMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.ExpenseManagement.DTO.OrganizationDTO;
import com.example.demo.ExpenseManagement.Entity.Organization;
import com.example.demo.ExpenseManagement.ExceptionController.ApplicationException;
import com.example.demo.ExpenseManagement.ExceptionController.ValidationException;
import com.example.demo.ExpenseManagement.Repository.OrganizationRepository;

@Service
public class OrganizationService {
    
    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private AddressService addressService;

    public OrganizationDTO getOrganizationById(Long organizationId) {
        
        Organization organization = null;
        try {
            organization = organizationRepository.findOrganizationById(organizationId);
            if(organization == null) {
                throw new ValidationException("No record found for the organization Id " + organizationId, HttpStatus.NOT_FOUND);
            }
            return this.mapOrganizationToOrganizationDTO(organization);
        }
        catch (ValidationException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while retrieving organization by Id "+ organizationId);
        }
    }

    public void addOrganization(OrganizationDTO organizationDTO) {

        Organization organization = null;
        try {
            if(organizationDTO == null ||
            (organizationDTO.getOrganizationName() instanceof String && (organizationDTO.getOrganizationName().isEmpty() || organizationDTO.getOrganizationName().length() > 40)) ||
            (organizationDTO.getOrganizationNumber() instanceof String && (organizationDTO.getOrganizationNumber().isEmpty() || organizationDTO.getOrganizationNumber().length() > 20)) ||
            (organizationDTO.getAddressId() == null)) {

                throw new ValidationException("Non null field value must not be null or empty and characters must not exceed limit.", HttpStatus.BAD_REQUEST);
            }
            organization = this.mapOrganizationDTOToOrganization(organizationDTO);
            organization.setAddress(addressService.getAddressById(organizationDTO.getAddressId()));
            organizationRepository.saveAndFlush(organization);
        }
        catch (ValidationException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while saving the organization.");
        }
    }

    public void updateOrganization(OrganizationDTO updatedOrganizationDTO) {

        Organization existingOrganization = null;
        try {
            existingOrganization = organizationRepository.findOrganizationById(updatedOrganizationDTO.getOrganizationId());

            if(existingOrganization == null) {
                throw new ValidationException("No record found to update for the organization ID" + updatedOrganizationDTO.getOrganizationId(), HttpStatus.NOT_FOUND);
            }

            if(updatedOrganizationDTO.getOrganizationName() instanceof String || ! (updatedOrganizationDTO.getOrganizationName().isEmpty())) {

                if(updatedOrganizationDTO.getOrganizationName().length() > 40)
                    throw new ValidationException("Organization name must not exceed 40 characters", HttpStatus.BAD_REQUEST);
                existingOrganization.setOrganizationName(updatedOrganizationDTO.getOrganizationName());
            }

            if(updatedOrganizationDTO.getOrganizationNumber() instanceof String && ! (updatedOrganizationDTO.getOrganizationNumber().isEmpty())) {

                if(updatedOrganizationDTO.getOrganizationNumber().length() > 20)
                    throw new ValidationException("Organization number must not exceed 20 characters", HttpStatus.BAD_REQUEST);
                existingOrganization.setOrganizationNumber(updatedOrganizationDTO.getOrganizationNumber());
            }

            if(updatedOrganizationDTO.getIsActive() instanceof Boolean) {

                existingOrganization.setIsActive(updatedOrganizationDTO.getIsActive());
            }

            if(updatedOrganizationDTO.getAddressId() != null) {
                existingOrganization.setAddress(addressService.getAddressById(updatedOrganizationDTO.getAddressId()));
            }
            organizationRepository.save(existingOrganization);
        }
        catch (ValidationException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while updating.");
        }
    }


    public OrganizationDTO mapOrganizationToOrganizationDTO(Organization organization) {
        
        OrganizationDTO organizationDTO = mapper.map(organization, OrganizationDTO.class);
        organizationDTO.setAddressId(organization.getAddress().getAddressId());
        return organizationDTO;
    }

    public Organization mapOrganizationDTOToOrganization(OrganizationDTO organizationDTO) {

        Organization organization = mapper.map(organizationDTO, Organization.class);
        organization.setAddress(addressService.getAddressById(organizationDTO.getAddressId()));
        return organization;
    }
}
