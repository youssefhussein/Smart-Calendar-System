package com.schedule.calendar;

import com.schedule.calendar.Models.User; 
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class TestingHM {

    // Replicating the logic to test directly within the test class for demonstration
    // In a real scenario, you'd test the actual method where this logic resides
    private String[] getRolesFromUserLogic(User user) {
         if (user == null || user.getUserRole() == null) {
             return new String[]{"USER"}; 
         }

         
         return user.getUserRole().split(",");
    }

    @Test
    public void testGetRoles_nullRoleDefaultsToUser() {
        User user = new User();
        user.setUserRole(null);
        String[] expectedRoles = {"USER"};

        String[] actualRoles = getRolesFromUserLogic(user);

        assertArrayEquals(expectedRoles, actualRoles, "User with null role should get 'USER' role");
    }
}
