package pt.com.bayonnesensei.salesInfo;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

@TestConfiguration
@EnableAutoConfiguration
@Import(SalesInfoApplication.class)
public class SalesInfoApplicationTestConfig {
}
