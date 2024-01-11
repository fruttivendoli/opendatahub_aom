import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibz.aom.Aom;
import it.unibz.aom.typesquare.Entity;
import it.unibz.parsers.data.DataParser;
import it.unibz.parsers.schema.SchemaParser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.List;

public class DataParserTest {

    private static Aom aom;
    private static ObjectNode data;

    @BeforeAll
    public static void setUp() {
        //Build Schema
        File file = new File(SchemaParserTest.class.getResource("/swaggerTypesExample.json").getFile());
        String jsonString = null;
        try {
            jsonString = new String(java.nio.file.Files.readAllBytes(file.toPath()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        ObjectNode swagger = null;
        try {
            swagger = new ObjectMapper().readValue(jsonString, ObjectNode.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        aom = new SchemaParser(swagger).getAom();
        System.out.println(aom);

        //Example response
        File responseFile = new File(DataParserTest.class.getResource("/exampleResponse1.json").getFile());
        String responseStr = null;
        try {
            responseStr = new String(java.nio.file.Files.readAllBytes(responseFile.toPath()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            data = new ObjectMapper().readValue(responseStr, ObjectNode.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /*
     {
      "Id": "BFEB2DDB0FD54AC9BC040053A5514A92_REDUCED",
      "Pdf": null,
      "Ranc": null,
      "Self": "https://tourism.opendatahub.com/v1/Event/BFEB2DDB0FD54AC9BC040053A5514A92_REDUCED",
      "Type": null,
      "_Meta": {
        "Id": "BFEB2DDB0FD54AC9BC040053A5514A92_REDUCED",
        "Type": "event",
        "Source": "lts",
        "Reduced": true,
        "LastUpdate": "2023-05-24T14:29:48.6122285",
        "UpdateInfo": null
      },
      "Active": true,
      "Detail": {
        "de": {
          "Title": "1000-Stufen-Schlucht im Vinschgau",
          "Header": null,
          "BaseText": null,
          "Keywords": null,
          "Language": "de",
          "MetaDesc": null,
          "AuthorTip": null,
          "IntroText": null,
          "MetaTitle": "1000-Stufen-Schlucht im Vinschgau | suedtirol.info",
          "SubHeader": null,
          "SafetyInfo": null,
          "ParkingInfo": null,
          "GetThereText": null,
          "EquipmentInfo": null,
          "AdditionalText": null,
          "PublicTransportationInfo": null
        },
        "it": {
          "Title": "Venosta: La \"valle dei 1000 gradini\"",
          "Header": null,
          "BaseText": null,
          "Keywords": null,
          "Language": "it",
          "MetaDesc": null,
          "AuthorTip": null,
          "IntroText": null,
          "MetaTitle": "Venosta: La \"valle dei 1000 gradini\" | suedtirol.info",
          "SubHeader": null,
          "SafetyInfo": null,
          "ParkingInfo": null,
          "GetThereText": null,
          "EquipmentInfo": null,
          "AdditionalText": null,
          "PublicTransportationInfo": null
        }
      },
      "OrgRID": null,
      "PayMet": null,
      "SignOn": null,
      "Source": "lts",
      "Ticket": null,
      "Topics": null,
      "DateEnd": "2022-06-01T00:00:00",
      "GpsInfo": [
        {
          "Gpstype": "position",
          "Altitude": null,
          "Latitude": 46.644273,
          "Longitude": 11.225259,
          "AltitudeUnitofMeasure": null
        }
      ],
      "Gpstype": "position",
      "Hashtag": {},
      "LTSTags": null,
      "Mapping": {},
      "ODHTags": [],
      "SmgTags": null,
      "Altitude": null,
      "GrpEvent": null,
      "Latitude": 46.644273,
      "DateBegin": "2022-06-01T00:00:00",
      "Districts": [],
      "EventDate": [
        {
          "To": "2022-06-01T00:00:00",
          "End": "17:30:00",
          "From": "2022-06-01T00:00:00",
          "Begin": "09:00:00",
          "Active": null,
          "DayRID": null,
          "Ticket": null,
          "GpsEast": null,
          "Entrance": null,
          "GpsNorth": null,
          "Cancelled": "0",
          "PriceFrom": null,
          "MaxPersons": null,
          "MinPersons": null,
          "SingleDays": false,
          "InscriptionTill": null,
          "EventCalculatedDay": null,
          "EventDateAdditionalInfo": null,
          "EventDateAdditionalTime": null
        }
      ],
      "GpsPoints": {
        "position": {
          "Gpstype": "position",
          "Altitude": null,
          "Latitude": 46.644273,
          "Longitude": 11.225259,
          "AltitudeUnitofMeasure": null
        }
      },
      "Longitude": 11.225259,
      "OdhActive": false,
      "Shortname": "1000-Stufen-Schlucht im Vinschgau",
      "SmgActive": false,
      "TopicRIDs": null,
      "DistrictId": null,
      "EventPrice": {},
      "LastChange": "2023-05-24T14:29:48.6122285",
      "DistrictIds": null,
      "EventPrices": {},
      "FirstImport": "2022-04-17T00:05:19.6546834",
      "HasLanguage": [
        "de",
        "it"
      ],
      "LicenseInfo": {
        "Author": "",
        "License": "CC0",
        "ClosedData": false,
        "LicenseHolder": "https://www.lts.it"
      },
      "PublishedOn": [],
      "ContactInfos": {
        "de": {
          "Tax": null,
          "Url": "",
          "Vat": null,
          "Area": null,
          "City": "Hafling Dorf",
          "Email": null,
          "Region": null,
          "Address": "",
          "LogoUrl": null,
          "Surname": null,
          "ZipCode": "39010",
          "Language": "de",
          "Faxnumber": null,
          "Givenname": null,
          "NamePrefix": null,
          "RegionCode": null,
          "CompanyName": null,
          "CountryCode": null,
          "CountryName": null,
          "Phonenumber": null
        },
        "it": {
          "Tax": null,
          "Url": "",
          "Vat": null,
          "Area": null,
          "City": "Avelengo paese",
          "Email": null,
          "Region": null,
          "Address": "",
          "LogoUrl": null,
          "Surname": null,
          "ZipCode": "39010",
          "Language": "it",
          "Faxnumber": null,
          "Givenname": null,
          "NamePrefix": null,
          "RegionCode": null,
          "CompanyName": null,
          "CountryCode": null,
          "CountryName": null,
          "Phonenumber": null
        }
      },
      "DistanceInfo": null,
      "EventBenefit": null,
      "EventBooking": {
        "BookableTo": null,
        "BookingUrl": {
          "de": {
            "Url": "https://mysuedtirol.info/de/veranstaltungen?eventid=BFEB2DDB0FD54AC9BC040053A5514A92"
          },
          "en": {
            "Url": "https://mysuedtirol.info/en/events?eventid=BFEB2DDB0FD54AC9BC040053A5514A92"
          },
          "it": {
            "Url": "https://mysuedtirol.info/it/eventi?eventid=BFEB2DDB0FD54AC9BC040053A5514A92"
          }
        },
        "BookableFrom": null,
        "AccommodationAssignment": null
      },
      "ImageGallery": null,
      "LocationInfo": {
        "TvInfo": {
          "Id": "5228229151CA11D18F1400A02427D15E",
          "Name": {
            "cs": "Hafling/Avelengo-Vöran/Verano-Meran 2000",
            "de": "Hafling - Vöran - Meran 2000",
            "en": "Hafling/Avelengo-Vöran/Verano-Meran 2000",
            "fr": "Hafling/Avelengo-Vöran/Verano-Meran 2000",
            "it": "Avelengo - Verano - Merano 2000",
            "nl": "Hafling/Avelengo-Vöran/Verano-Meran 2000",
            "pl": "Hafling/Avelengo-Vöran/Verano-Meran 2000",
            "ru": "Хафлинг - Фёран - Меран/Avelengo -  Фёран-Verano - Меран 2000"
          },
          "Self": "https://tourism.opendatahub.com/v1/TourismAssociation/5228229151CA11D18F1400A02427D15E"
        },
        "AreaInfo": null,
        "RegionInfo": {
          "Id": "D2633A20C24E11D18F1B006097B8970B",
          "Name": {
            "cs": "Meran/Merano a okolí",
            "de": "Meran und Umgebung",
            "en": "Meran/Merano and environs",
            "fr": "Meran/Merano et environs",
            "it": "Merano e dintorni",
            "nl": "Meran/Merano en omgeving",
            "pl": "Meran/Merano i okolice",
            "ru": "Меран/Merano и окрестности"
          },
          "Self": "https://tourism.opendatahub.com/v1/Region/D2633A20C24E11D18F1B006097B8970B"
        },
        "DistrictInfo": {
          "Id": "79CBD64E51C911D18F1400A02427D15E",
          "Name": {
            "cs": "Hafling Dorf/Avelengo Paese",
            "de": "Hafling Dorf",
            "en": "Avelengo Paese/Hafling Dorf",
            "fr": "Avelengo Paese/Hafling Dorf",
            "it": "Avelengo Paese",
            "nl": "Avelengo Paese/Hafling Dorf",
            "pl": "Avelengo Paese/Hafling Dorf",
            "ru": "Авеленго Паэзе"
          },
          "Self": "https://tourism.opendatahub.com/v1/District/79CBD64E51C911D18F1400A02427D15E"
        },
        "MunicipalityInfo": {
          "Id": "FD746CCBD5484A84AA09A75C8ACBA35D",
          "Name": {
            "cs": "Hafling/Avelengo",
            "de": "Hafling",
            "en": "Hafling/Avelengo",
            "fr": "Hafling/Avelengo",
            "it": "Avelengo",
            "nl": "Hafling/Avelengo",
            "pl": "Hafling/Avelengo",
            "ru": "Хафлинг/Avelengo"
          },
          "Self": "https://tourism.opendatahub.com/v1/Municipality/FD746CCBD5484A84AA09A75C8ACBA35D"
        }
      },
      "EventDatesEnd": [
        "2022-06-01T00:00:00"
      ],
      "NextBeginDate": "2022-06-01T09:00:00",
      "EventPublisher": null,
      "OrganizerInfos": {},
      "EventDatesBegin": [
        "2022-06-01T00:00:00"
      ],
      "EventDateCounter": 1,
      "ClassificationRID": null,
      "EventCrossSelling": null,
      "EventDescAdditional": {},
      "EventAdditionalInfos": {},
      "AltitudeUnitofMeasure": null,
      "EventOperationScheduleOverview": null
    }
     */

    @Test
    public void parseFirstLevel() {
        DataParser dataParser = new DataParser(aom, data, "#/components/schemas/EventLinked");
        List<Entity> events = dataParser.parse();
        Entity event = events.get(0);
        assertEquals(event.getProperty("Id"), "BFEB2DDB0FD54AC9BC040053A5514A92_REDUCED");
        assertNull(event.getProperty("Pdf"));
        assertNull(event.getProperty("Ranc"));
        assertEquals(event.getProperty("Self"), "https://tourism.opendatahub.com/v1/Event/BFEB2DDB0FD54AC9BC040053A5514A92_REDUCED");
        assertNull(event.getProperty("Type"));
        assertEquals(event.getProperty("Active"), true);
        assertNull(event.getProperty("OrgRID"));
        assertNull(event.getProperty("PayMet"));
        assertNull(event.getProperty("SignOn"));
        assertEquals(event.getProperty("Source"), "lts");
        assertNull(event.getProperty("Ticket"));
        assertEquals(event.getProperty("DateEnd"), "2022-06-01T00:00:00");
        assertEquals(event.getProperty("Gpstype"), "position");
        assertEquals(event.getProperty("Latitude"), 46.644273);
        assertEquals(event.getProperty("DateBegin"), "2022-06-01T00:00:00");
        assertEquals(event.getProperty("Longitude"), 11.225259);
        assertEquals(event.getProperty("OdhActive"), false);
        assertEquals(event.getProperty("Shortname"), "1000-Stufen-Schlucht im Vinschgau");
        assertEquals(event.getProperty("SmgActive"), false);
        assertEquals(event.getProperty("FirstImport"), "2022-04-17T00:05:19.6546834");
        assertEquals(event.getProperty("LastChange"), "2023-05-24T14:29:48.6122285");
        assertArrayEquals((String[]) event.getProperty("HasLanguage"), new String[]{"de", "it"});
        assertEquals(event.getProperty("NextBeginDate"), "2022-06-01T09:00:00");
    }

}
