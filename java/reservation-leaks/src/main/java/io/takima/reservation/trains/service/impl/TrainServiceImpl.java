package io.takima.reservation.trains.service.impl;

import io.takima.reservation.exception.InvalidInputException;
import io.takima.reservation.trains.domain.Train;
import io.takima.reservation.trains.dto.requests.TrainCreationRequest;
import io.takima.reservation.trains.repository.TrainDao;
import io.takima.reservation.trains.service.CarService;
import io.takima.reservation.trains.service.TrainService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Service
public class TrainServiceImpl implements TrainService {

    private final Logger logger = LoggerFactory.getLogger(TrainServiceImpl.class);

    TrainDao trainDao;

    CarService carService;

    // Fix circular DI TravelService <-> TrainService
    // TravelService travelService;

    public Page<Train> getTrains(PageRequest pageRequest) {
        logger.info(String.format("getTrains (page: %d, size: %d)", pageRequest.getPageNumber(), pageRequest.getPageSize()));
        return trainDao.findAll(pageRequest);
    }

    @Override
    public Train getTrain(long trainId) {
        logger.info(String.format("getTrain (id: %d)", trainId));
        return trainDao.findById(trainId).orElseThrow(() -> new NoSuchElementException(String.format("No Train with id %d", trainId)));
    }

    @Override
    public Train create(TrainCreationRequest request, boolean manualTravelAssign) {
        logger.info(String.format("create (hash: %d)", request.hashCode()));
        if (request.getCars() == null || request.getCars().isEmpty()) {
            throw new InvalidInputException("Train creation request is missing the cars");
        }

        var cars = carService.create(request);

        var train = Train.from(cars);

        trainDao.save(train);

        cars.forEach(car -> car.setTrain(train));


        if (!manualTravelAssign) {
            logger.warn(String.format("Assigning random travel to freshly created train (id: %d)", train.getTrainId()));
            // travelService.assignRandomTravel(trainDao.save(finalTrain));
        }

        return train;
    }
}
