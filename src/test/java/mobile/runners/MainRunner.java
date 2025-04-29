package mobile.runners;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.apache.commons.lang3.BooleanUtils;
import org.junit.runner.RunWith;
import mobile.utils.SerenityPropertyHelper;
import java.io.File;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "mobile.steps",
        tags = "@smoke"
)
public class MainRunner {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(MainRunner.class);

    public static void logSerenityReportDetails() {
        if (BooleanUtils.toBoolean(SerenityPropertyHelper.getProperty("logging.log4j"))) {
            String reportOutputFolder = System.getProperty("user.dir") + "/target/site/serenity/";
            File reportsFolder = new File(reportOutputFolder);
            File[] listOfFiles = reportsFolder.listFiles();
            if (listOfFiles != null) {
                for (File listofFile : listOfFiles) {
                    if (listofFile.isFile() && listofFile.toString().endsWith(".json")) {
                        String jsonFilePath = listofFile.toString();
                        logger.info("Serenity Report JSON File: {}", jsonFilePath);
                    }
                }
            } else {
                logger.warn("No files found in the Serenity report output folder: {}", reportOutputFolder);
            }
        }
    }
}