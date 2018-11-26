package kr.geun.o.app.bbs.controller.api;

import kr.geun.o.app.bbs.dto.BbsCategoryDTO;
import kr.geun.o.app.bbs.service.BbsCategoryApiService;
import kr.geun.o.common.response.ResData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 글 관련 Api
 *
 * @author akageun
 */
@RestController
@RequestMapping("/api/category/v1")
public class BbsCategoryApiController {

	@Autowired
	private BbsCategoryApiService bbsCategoryApiService;

	@GetMapping("/search")
	public ResponseEntity<ResData> categorySearch(@Valid BbsCategoryDTO.Search param, BindingResult result) {
		if (result.hasErrors()) {
			return ResponseEntity.badRequest().body(ResData.of(result));
		}

		return ResponseEntity.ok().body(ResData.of(bbsCategoryApiService.search(param.getKeyword()), "성공"));
	}
}