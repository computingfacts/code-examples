package uk.gov.dwp.dwpservice.util;

import lombok.extern.slf4j.Slf4j;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

/**
 *
 * @author joseph
 */
@Slf4j
public class GeoUtilTest {

    /**
     * Test of isWithinCoordinates method, of class GeoUtil.
     */
    @Test
    public void testIsWithinCoordinates() {
        log.info("testIsWithinCoordinates");
        double lat1 = 51.509865;
        double lon1 = -0.118092;

        double lat2 = 51.6553959;
        double lon2 = 0.0572553;
        int radius = 50;

        boolean isWithin = GeoUtil.isWithinCoordinates(lat1, lat2, lon1, lon2, radius);

        assertThat(isWithin).isTrue();

    }

    @Test
    public void testIsNotWithinCoordinates() {
        log.info("testIsNotWithinCoordinates");
        double lat1 = 51.509865;
        double lon1 = -0.118092;

        double lat2 = 51.6553959;
        double lon2 = 0.0572553;
        int radius = 5;

        boolean isWithin = GeoUtil.isWithinCoordinates(lat1, lat2, lon1, lon2, radius);

        assertThat(isWithin).isFalse();

    }

}
