package com.example.demo.ExpenseManagement.Service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.ExpenseManagement.DTO.ApprovalsDTO;
import com.example.demo.ExpenseManagement.Entity.Approvals;
import com.example.demo.ExpenseManagement.ExceptionController.ApplicationException;
import com.example.demo.ExpenseManagement.ExceptionController.ValidationException;
import com.example.demo.ExpenseManagement.Repository.ApprovalsRepository;
import com.example.demo.ExpenseManagement.Repository.ExpenseRepository;
import com.example.demo.ExpenseManagement.Repository.UserRepository;

@Service
public class ApprovalsService {

    @Autowired
    private ApprovalsRepository approvalsRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExpenseRepository expenseRepository;
    
    public ApprovalsDTO getApprovalById(Long approvalId) {
        
        Approvals approval = null;
        try {
            approval = approvalsRepository.findByApprovalId(approvalId);
            if(approval == null) {
                throw new ValidationException("No approval found for the approval id " + approvalId, HttpStatus.NOT_FOUND);
            }
            return this.mapApprovalsToApprovalsDTO(approval);
        }
        catch (ValidationException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while retrieving approval by approval Id " + approvalId);
        }
    }

    public List<ApprovalsDTO> getApprovalsByApprovedUserId(Long userId) {
        
        List<ApprovalsDTO> approvalsDTO = new ArrayList<>();
        List<Approvals> approvals = new ArrayList<>();
        try {
            approvals = approvalsRepository.findByApprovedUser(userId);
            
            if(approvals == null) {
                throw new ValidationException("No approval found for the user id " + userId, HttpStatus.NOT_FOUND);
            }
            for (Approvals approval : approvals) {
                approvalsDTO.add(this.mapApprovalsToApprovalsDTO(approval));
            }
            return approvalsDTO;
        }
        catch (ValidationException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while retrieving approvals by userId " + userId);
        }
    }

    public List<ApprovalsDTO> getApprovalsByExpenseId(Long expenseId) {
        List<ApprovalsDTO> approvalsDTO = new ArrayList<>();
        List<Approvals> approvals = new ArrayList<>();
        try {
            approvals = approvalsRepository.findByExpenseId(expenseId);
            
            if(approvals == null) {
                throw new ValidationException("No approval found for the expense id " + expenseId, HttpStatus.NOT_FOUND);
            }
            for (Approvals approval : approvals) {
                approvalsDTO.add(this.mapApprovalsToApprovalsDTO(approval));
            }
            return approvalsDTO;
        }
        catch (ValidationException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while retrieving approvals by expenseId " + expenseId);
        }
    }

    public void addApproval(Long approverId, Long expenseId) {
        
        Approvals approval = new Approvals();
        try {
            if(approverId instanceof Long && expenseId instanceof Long) {

                approval.setApprovedBy(userRepository.findUserById(approverId));
                approval.setExpense(expenseRepository.findExpenseById(expenseId));
                approval.setApprovedDate(ZonedDateTime.now(ZoneId.of("UTC")));
                    
                approvalsRepository.saveAndFlush(approval);
            }
            else {
                throw new ValidationException("Non null field value must not be null or empty", HttpStatus.BAD_REQUEST);
            }
        }
        catch (ValidationException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ApplicationException("An unexpected error occurred while saving approvals.");
        }
    }

    public ApprovalsDTO mapApprovalsToApprovalsDTO(Approvals approval) {
        
        ApprovalsDTO approvalDTO = mapper.map(approval, ApprovalsDTO.class);
        approvalDTO.setUserId(approval.getApprovedBy().getUserId());
        approvalDTO.setApprovedDate(approval.getApprovedDate().withZoneSameInstant(ZoneId.systemDefault()));
        approvalDTO.setExpenseId(approval.getExpense().getExpenseId());
        return approvalDTO;
    }
}
