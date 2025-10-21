
//
//package com.example.demo.controller;
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.example.demo.service.BackblazeService;
//
//@RestController
//@RequestMapping("/api/images")
//public class ImageController {
//    
//    @Autowired
//    private BackblazeService backblazeService;
//    
//    @PostMapping("/upload")
//    public ResponseEntity<String> uploadImage(
//            @RequestParam MultipartFile file,
//            @RequestParam("productId") String productId) {
//        try {
//            String imageUrl = backblazeService.uploadProductImage(file, productId);
//            return ResponseEntity.ok(imageUrl);
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("Upload failed");
//        }
//    }
//}
=======

// package com.example.demo.controller;


// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.multipart.MultipartFile;

// import com.example.demo.service.BackblazeService;

// @RestController
// @RequestMapping("/api/images")
// public class ImageController {
    
//     @Autowired
//     private BackblazeService backblazeService;
    
//     @PostMapping("/upload")
//     public ResponseEntity<String> uploadImage(
//             @RequestParam MultipartFile file,
//             @RequestParam("productId") String productId) {
//         try {
//             String imageUrl = backblazeService.uploadProductImage(file, productId);
//             return ResponseEntity.ok(imageUrl);
//         } catch (Exception e) {
//             return ResponseEntity.status(500).body("Upload failed");
//         }
//     }
// }

