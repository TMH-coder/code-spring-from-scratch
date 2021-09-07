import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.turbo.TurboFilter;
import ch.qos.logback.core.pattern.DynamicConverter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Slf4j
public class LoggerTest {
    @Test
    public void test(){
//可以用这个来设置开启debug
        ch.qos.logback.classic.LoggerContext loggerContext = (ch.qos.logback.classic.LoggerContext) LoggerFactory.getILoggerFactory();
        Logger logger = loggerContext.getLogger("root");
        logger.setLevel(Level.DEBUG);
        log.debug("set ok");
        log.info("info");
    };

}
