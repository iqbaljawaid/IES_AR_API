package in.globalit.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import in.globalit.binding.CitizenBinder;
import in.globalit.constants.AppConstants;
import in.globalit.entity.CitizenApplicationEntity;
import in.globalit.entity.UserEntity;
import in.globalit.props.AppProperties;
import in.globalit.repository.CitizenRepo;
import in.globalit.repository.UserRepo;
import in.globalit.request.CitizenRequest;
import in.globalit.service.CitizenService;

@Service
public class CitizenServiceImpl implements CitizenService {

	public String URL = AppConstants.SAVE_CITIZEN_URL;

	@Autowired
	private CitizenRepo citizenRepo;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private AppProperties appProps;

	@Override
	public String saveCitizenData(CitizenBinder binder) {
		Long userId = 1l;// here we are hard coding the value but we will take user id from ui in the
							// form of session
		WebClient webClient = WebClient.create();
		if (binder.getSsn().length() == 9) {
			CitizenRequest citizenRequest = new CitizenRequest();
			BeanUtils.copyProperties(binder, citizenRequest);
			
			CitizenApplicationEntity citizenEntity = webClient.post().uri(URL).bodyValue(citizenRequest).retrieve()
					.bodyToMono(CitizenApplicationEntity.class).block();

			Optional<UserEntity> findById = userRepo.findById(userId);
			if (findById.isPresent()) {
				UserEntity userEntity = findById.get();
				citizenEntity.setUser(userEntity);
				citizenEntity.setEmail(binder.getEmail());
				citizenEntity.setMobNo(binder.getMobNo());
				if (citizenRepo.findBySsn(citizenEntity.getSsn()) == null && citizenEntity.getState()
						.contains(appProps.getMessages().get(AppConstants.CITIZEN_STATE_KEY))) {
					citizenRepo.save(citizenEntity);
					return AppConstants.SUCCESS_STR;
				} else if (citizenRepo.findBySsn(citizenEntity.getSsn()) != null) {

					return appProps.getMessages().get(AppConstants.ONE_BENEFIT_PER_USER);
				}

				else {
					return appProps.getMessages().get(AppConstants.RI_STATE_CITIZEN_BENEFIT);
				}
			}

		}
		return appProps.getMessages().get(AppConstants.INVALID_SSN_KEY);

	}

	@Override
	public List<CitizenApplicationEntity> getAllCitizensData() {

		return citizenRepo.findAll();
	}

}
