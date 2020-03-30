package wrss.rest.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ResponseHeader;
import wrss.rest.models.request.LoginRequestModel;

@RestController
public class AuthenticationController {

	@ApiOperation("User login")
	@ApiResponse(code = 200, message = "Response Headers", responseHeaders = {
			@ResponseHeader(name = "authorization", description = "Bearer <JWT value>"),
			@ResponseHeader(name = "userId", description = "<UserId value>") })
	@PostMapping("/api/login")
	public void fakeLogin(@RequestBody LoginRequestModel loginRequestModel) {
		throw new IllegalStateException(
				"This method should not be called. This method is implemented by Spring Security");
	}
}
