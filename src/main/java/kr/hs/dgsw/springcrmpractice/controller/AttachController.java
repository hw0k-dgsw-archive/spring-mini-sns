package kr.hs.dgsw.springcrmpractice.controller;

import kr.hs.dgsw.springcrmpractice.domain.Attachment;
import kr.hs.dgsw.springcrmpractice.service.AttachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class AttachController {

    private final AttachService attachService;

    @Autowired
    public AttachController(AttachService attachService) {
        this.attachService = attachService;
    }

    @PostMapping("/attach")
    public Attachment attach(@RequestPart MultipartFile file) {
        return attachService.attach(file);
    }

    @GetMapping("/attach/{file:.+}")
    public ResponseEntity<Resource> serve(@PathVariable String file) {
        return attachService.serve(file);
    }
}
