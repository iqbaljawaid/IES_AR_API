package in.globalit.service;

import java.util.List;

import in.globalit.binding.CitizenBinder;
import in.globalit.entity.CitizenApplicationEntity;

public interface CitizenService {
	
	public String saveCitizenData(CitizenBinder binder);
	public List<CitizenApplicationEntity> getAllCitizensData();

}
