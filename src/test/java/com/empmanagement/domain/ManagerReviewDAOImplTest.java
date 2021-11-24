package com.empmanagement.daoimpl;

import com.empmanagement.domain.EmployeePeer;
import com.empmanagement.domain.EmployeePeerRowMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertEquals;

/*
 * @author: Dhruv Bharatbhai Patel - B00868931
 * */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("Test suites for ManagerReviewDAOImpl class")
public class ManagerReviewDAOImplTest {
    private static final String TABLE_EMPLOYEES="employee";
    private static final String TABLE_TEAMS="teams";
    private static final String QUERY_GET_MANAGERS_PART="SELECT s.managerId as empId,u.empName from (select * from "+ TABLE_TEAMS +" t inner join "+TABLE_EMPLOYEES+" e on t.teams=e.team where e.empId= ? ) as s inner join (select * from "+TABLE_EMPLOYEES+" emp) as u on s.managerId=u.empId";

    private static final String EMPLOYEE_ID="1000";
    private static final String EMPLOYEE_NAME="Dhruv";

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private ManagerReviewDAOImpl managerReviewDAO;

    @SuppressWarnings("deprecation")
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(managerReviewDAO, "jdbcTemplate", jdbcTemplate);
    }

    @Test
    @DisplayName("Test case for getManagers method")
    public void getManagersTest(){
        EmployeePeer employeePeer=setPeer();

        ReflectionTestUtils.setField(managerReviewDAO, "jdbcTemplate", jdbcTemplate);

        Mockito.when(jdbcTemplate.query(QUERY_GET_MANAGERS_PART,new Object[]{"1000"},new EmployeePeerRowMapper())).thenReturn(Stream
                .of(employeePeer).collect(Collectors.toList()));

        assertEquals(0,managerReviewDAO.getManagers("1000").size());
    }

    private EmployeePeer setPeer(){
        EmployeePeer employeePeer=new EmployeePeer();
        employeePeer.setEmpId(EMPLOYEE_ID);
        employeePeer.setEmpName(EMPLOYEE_NAME);

        return employeePeer;
    }
}
