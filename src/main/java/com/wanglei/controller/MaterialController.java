package com.wanglei.controller;

import com.wanglei.pojo.Material;
import com.wanglei.pojo.ResponseMessage;
import com.wanglei.service.MaterialService;
import com.wanglei.util.CheckToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    @CheckToken
    @RequestMapping(value = {"/material"}, method = RequestMethod.GET)
    public ResponseMessage materialController(@RequestParam(value = "keywords",required = false) String[] keywords, @RequestParam(value = "page", defaultValue = "0") Integer page){
        List<Material> materials = materialService.getMaterialList(keywords, page);
        Integer count = materialService.getCount(keywords);
        return new ResponseMessage<>(materials, count).success();
    }

    @CheckToken
    @RequestMapping(value = {"/material/add"}, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity materialAddController(@RequestParam(value = "material_name") String materialName){
        if(materialService.insertMaterial(materialName)){
            return ResponseEntity.status(200).body(new ResponseMessage<>(null,0).success());
        }
        return ResponseEntity.status(202).body(new ResponseMessage<>(null,0).error(202,"failed to add!"));
    }

    @CheckToken
    @RequestMapping(value = {"/material/delete"}, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity materialDeleteController(@RequestParam(value = "material_id") Integer materialId){
        try {
            materialService.deleteMaterial(materialId);
            return ResponseEntity.status(200).body(new ResponseMessage<>(null,0).success());
        }
        catch (Error e) {
            return ResponseEntity.status(202).body(new ResponseMessage<>(null,0).error(202,"failed to delete!"));
        }
    }

    @CheckToken
    @RequestMapping(value = {"/material/update"}, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity materialUpdateController(@RequestParam(value = "material_id") Integer materialId, @RequestParam(value = "newName") String newName){
        if(materialService.updateMaterial(materialId, newName)){
            return ResponseEntity.status(200).body(new ResponseMessage<>(null,0).success());
        }
        return ResponseEntity.status(202).body(new ResponseMessage<>(null,0).error(202,"failed to update!"));
    }
}
