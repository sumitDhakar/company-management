package com.dollop.app.entity;

import java.sql.Timestamp;

import com.dollop.app.entity.status.AttendenceStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Attendance {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

    private String status=AttendenceStatus.Incompelete.toString();;
    
    @ManyToOne
    @JoinColumn(name = "userId")
    private Users userId;
    
    private Timestamp inTime;

    private Timestamp outTime;
    
    private Integer checkedBy;
  //colum text
    private String note;
    
    private Timestamp checkedAt;
  //colum text
    private String rejectReason;
    
    private Boolean deleted =false;
    
    private Boolean isMispunched=true;
    
}

