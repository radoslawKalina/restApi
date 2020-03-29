package wrss.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {
	

	@ExceptionHandler
	public ResponseEntity<ExceptionResponseModel> handleException(Exception exc) {
		
		ExceptionResponseModel model = new ExceptionResponseModel();
		model.setMessage(exc.getMessage());
		
		return new ResponseEntity<>(model, HttpStatus.BAD_REQUEST);
			
	}

}
