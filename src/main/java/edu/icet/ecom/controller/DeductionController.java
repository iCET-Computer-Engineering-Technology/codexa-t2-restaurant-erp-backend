package edu.icet.ecom.controller;

import edu.icet.ecom.entity.Deduction;
import edu.icet.ecom.service.DeductionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/deduction")
@CrossOrigin
@RequiredArgsConstructor
public class DeductionController {
    private final DeductionService service;
    @GetMapping("/get-all")
    public List<Deduction> getAll(){
        return service.getDeduction();
    }

    @PostMapping("/add")
    public Boolean save(@RequestBody Deduction deduction){
        return service.addDeduction(deduction);
    }

    @PutMapping("/update")
    public Boolean update(@RequestBody Deduction deduction){
        return service.updateDeduction(deduction);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Integer id){
        service.deleteDeduction(id);
    }

}
