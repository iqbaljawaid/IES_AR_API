package in.globalit.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import in.globalit.binding.CitizenBinder;
import in.globalit.constants.AppConstants;
import in.globalit.entity.CitizenApplicationEntity;
import in.globalit.props.AppProperties;
import in.globalit.service.CitizenService;

@RestController
public class CitizenRestController {
	
	@Autowired
	private CitizenService citizenService;
	
	@Autowired
	private AppProperties appProps;
	
	@PostMapping("/save")
	public ResponseEntity<String> saveCitizenDtls(@RequestBody CitizenBinder binder){
		String citizenStatus = citizenService.saveCitizenData(binder);
		if(citizenStatus.contains(AppConstants.SUCCESS_STR)) {
			return new ResponseEntity<String>(appProps.getMessages().get(AppConstants.APPLICATION_CREATION_SUCCESS_KEY),HttpStatus.CREATED);
		}else {
			return new ResponseEntity<String>(citizenStatus,HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@GetMapping("/viewAll")
	public List<CitizenApplicationEntity> viewAllCitizenData(){
		return citizenService.getAllCitizensData();
	}

}
