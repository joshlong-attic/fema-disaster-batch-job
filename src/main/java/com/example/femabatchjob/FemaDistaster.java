package com.example.femabatchjob;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FemaDistaster {

    private String femaDeclarationString;
    private String disasterNumber;
    private String state;
    private String declarationType;
    private String declarationDate;
    private String fyDeclared;
    private String incidentType;
    private String declarationTitle;
    private String ihProgramDeclared;
    private String iaProgramDeclared;
    private String paProgramDeclared;
    private String hmProgramDeclared;
    private String incidentBeginDate;
    private String incidentEndDate;
    private String disasterCloseoutDate;
    private String fipsStateCode;
    private String fipsCountyCode;
    private String placeCode;
    private String designatedArea;
    private String declarationRequestNumber;
    private String hash;
    private String lastRefresh;
    private String id;

}
