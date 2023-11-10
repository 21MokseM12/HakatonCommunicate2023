package service;

import config.DataPreparation;
import ru.redcom.lib.integration.api.client.dadata.DaDataClient;
import ru.redcom.lib.integration.api.client.dadata.DaDataClientFactory;
import ru.redcom.lib.integration.api.client.dadata.DaDataException;
import ru.redcom.lib.integration.api.client.dadata.dto.*;

public class DaDataAPIService {
    private DaDataClient daDataClient = DaDataClientFactory.getInstance(DataPreparation.API_KEY, DataPreparation.API_SECRET_KEY);
    private String request = "{\"lat\": " + DataPreparation.lat + ", \"lon\": " + DataPreparation.lon +
            ", \"radius_meters\": " + DataPreparation.radius_meters + "}";

    public void getAdress() throws DaDataException {
        Phone adress = this.daDataClient.cleanPhone("+79303569144");
        System.out.println(request);

        System.out.println(adress);
    }
}
