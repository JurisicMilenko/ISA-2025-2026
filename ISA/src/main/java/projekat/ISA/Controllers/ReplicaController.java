package projekat.ISA.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import projekat.ISA.Config.GCounter;

@RestController
@RequestMapping("/internal")
public class ReplicaController {

	@Autowired
	private GCounter gCounter;

    @PostMapping("/sync")
    public void sync(@RequestBody GCounter incoming) {
        gCounter.merge(incoming);
    }
}
