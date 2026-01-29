package projekat.ISA.Controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import projekat.ISA.Config.GCounter;
import projekat.ISA.Services.GCounterService;

@RestController
@RequestMapping("/internal")
public class ReplicaController {

	@Autowired
	private GCounterService gCounter;

    @PostMapping("/sync")
    public void sync(@RequestBody Map<Long, GCounter> incoming) {
        gCounter.merge(incoming);
    }
}
