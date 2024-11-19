package candyCo;

import org.springframework.boot.SpringApplication;

public class TestBackendExamApplication {

    public static void main(String[] args) {
        SpringApplication.from(CandyCoApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
