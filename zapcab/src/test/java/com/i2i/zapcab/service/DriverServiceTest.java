package com.i2i.zapcab.service;

import static com.i2i.zapcab.common.ZapCabConstant.INITIAL_DRIVER_STATUS;
import com.i2i.zapcab.model.Driver;
import com.i2i.zapcab.repository.DriverRepository;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class DriverServiceTest {

    @InjectMocks
    DriverServiceImpl driverServiceImpl;

    @Mock
    DriverRepository driverRepository;


    @Test
    public void testSaveDriver() {
        Driver driver = new Driver();
        driver.setId("e1234-abc");
        Mockito.when(driverRepository.save(Mockito.any())).thenReturn(driver);
        driverServiceImpl.saveDriver(new Driver());
    }

//    @Test
//    public void testSaveDriverWithException() {
//        Assertions.assertThrows(Exception.class, () -> {
//            driverServiceImpl.saveDriver(null);
//        });
//    }

    @Test
    public void testUpdateDriverRatings() {
        Driver driver = new Driver();
        driver.setId("e1234-abc");
        driver.setRatings(4);
        Mockito.when(driverRepository.findById("e1234-abc")).thenReturn(Optional.of(new Driver()));
        driver.setRatings(5);
        driverServiceImpl.saveDriver(new Driver());
    }
}
