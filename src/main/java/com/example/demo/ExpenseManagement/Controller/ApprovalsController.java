package com.example.demo.ExpenseManagement.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.ExpenseManagement.DTO.ApprovalsDTO;
import com.example.demo.ExpenseManagement.Service.ApprovalsService;

@RestController
@RequestMapping("/approvals")
public class ApprovalsController {
    
    @Autowired
    private ApprovalsService approvalsService;

    //get all approvals done by an user(approver)
    //admin's access
    @GetMapping
    public ResponseEntity<List<ApprovalsDTO>> getAllApprovalsByUser(@RequestParam("userId") Long userId) {
        
        List<ApprovalsDTO> approvalsDTOList = approvalsService.getApprovalsByApprovedUserId(userId);
        if(approvalsDTOList != null) {
            return new ResponseEntity<>(approvalsDTOList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //admin's access
    @GetMapping("/")
    public ResponseEntity<ApprovalsDTO> getApprovalByApprovalId(@RequestParam("approvalId") Long approvalId) {
        
        ApprovalsDTO approval = approvalsService.getApprovalById(approvalId);
        if(approval != null) {
            return new ResponseEntity<>(approval, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //admin's access
    @GetMapping("/expense/{expenseId}")
    public ResponseEntity<List<ApprovalsDTO>> getApprovalByExpenseId(@PathVariable("expenseId") Long expenseId) {

        List<ApprovalsDTO> approval = approvalsService.getApprovalsByExpenseId(expenseId);
        if(approval != null) {
            return new ResponseEntity<>(approval, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
