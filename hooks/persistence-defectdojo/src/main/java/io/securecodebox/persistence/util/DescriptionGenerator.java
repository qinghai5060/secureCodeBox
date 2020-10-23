/*
 *
 *  SecureCodeBox (SCB)
 *  Copyright 2015-2018 iteratec GmbH
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  	http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * /
 */
package io.securecodebox.persistence.util;

import io.securecodebox.models.V1Scan;
import io.securecodebox.persistence.exceptions.DefectDojoPersistenceException;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
public class DescriptionGenerator {

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    protected static final String TIME_FORMAT = "dd.MM.yyyy HH:mm:ss";

    Clock clock = Clock.systemDefaultZone();

    public String generate(V1Scan scan){
        return MessageFormat.format("#{0}  \nTime: {1}  \nScanType: {2}  \nParameters: [{3}]",
                getDefectDojoScanName(scan),
                currentTime(),
                scan.getSpec().getScanType(),
                String.join(",", scan.getSpec().getParameters())
        );
    }

    private String currentTime() {
        return LocalDateTime.now(clock).format(DateTimeFormatter.ofPattern(TIME_FORMAT));
    }

    /**
     * Returns the current date as string based on the DATE_FORMAT.
     *
     * @return the current date as string based on the DATE_FORMAT.
     */
    public String currentDate() {
        return LocalDate.now(clock).format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    public void setClock(Clock clock) {
        this.clock = clock;
    }

    public String getDefectDojoScanName(V1Scan scan){
        return this.getDefectDojoScanName(scan.getSpec().getScanType());
    }

    /**
     * Returns the DefectDojo specific scanName for the given scb scbSecurityTestName.
     * @param scbSecurityTestName The scb scbSecurityTestName to return the corresponding DefectDojo scanName for.
     * @return The DefectDojo specific scanName for the given scb scbSecurityTestName.
     */
    public String getDefectDojoScanName(String scbSecurityTestName) {
        if(scbSecurityTestName.startsWith("zap")) {
            return "ZAP Scan";
        }

        Map<String, String> scannerDefectDojoMapping = new HashMap<>();

        // Officially supported by secureCodeBox
        scannerDefectDojoMapping.put("arachni", "Arachni Scan");
        scannerDefectDojoMapping.put("nmap", "Nmap Scan");
        scannerDefectDojoMapping.put("zap", "ZAP Scan");

        // Map amass-nmap raw results to be imported as Nmap Results
        scannerDefectDojoMapping.put("amass-nmap", "Nmap Scan");

        // Nikto is a supported tool as well but currently not accessible for supported import.
        // Nikto thus will use Generic Findings Import.

        // Can be used by 3rd party integrations to
        // import these scan results directly into defectdojo
        scannerDefectDojoMapping.put("appspider", "AppSpider Scan");
        scannerDefectDojoMapping.put("bandit", "Bandit Scan");
        scannerDefectDojoMapping.put("burp", "Burp Scan");
        scannerDefectDojoMapping.put("checkmarx", "Checkmarx Scan");
        scannerDefectDojoMapping.put("dependencycheck", "Dependency Check Scan");
        scannerDefectDojoMapping.put("gosec", "Gosec Scanner");
        scannerDefectDojoMapping.put("nessus", "Nessus Scan");
        scannerDefectDojoMapping.put("nexpose", "Nexpose Scan");
        scannerDefectDojoMapping.put("nodesecurityplattform", "Node Security Platform Scan");
        scannerDefectDojoMapping.put("openvas", "OpenVAS CSV");
        scannerDefectDojoMapping.put("qualys", "Qualys Scan");
        scannerDefectDojoMapping.put("qualyswebapp", "Qualys Webapp Scan");
        scannerDefectDojoMapping.put("retirejs", "Retire.js Scan");
        scannerDefectDojoMapping.put("skf", "SKF Scan");
        scannerDefectDojoMapping.put("ssllabs", "SSL Labs Scan");
        scannerDefectDojoMapping.put("snyk", "Snyk Scan");
        scannerDefectDojoMapping.put("trustwave", "Trustwave Scan (CSV)");
        scannerDefectDojoMapping.put("vgg", "VCG Scan");
        scannerDefectDojoMapping.put("veracode", "Veracode Scan");

        return scannerDefectDojoMapping.getOrDefault(scbSecurityTestName, "Generic Findings Import");
    }
}
