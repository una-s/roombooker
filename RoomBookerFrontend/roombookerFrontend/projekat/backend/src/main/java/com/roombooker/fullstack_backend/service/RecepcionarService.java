package com.roombooker.fullstack_backend.service;

import com.roombooker.fullstack_backend.JPARepo.RecepcionarRepository;
import com.roombooker.fullstack_backend.dto.GostDTO;
import com.roombooker.fullstack_backend.dto.RecepcionarDTO;
import com.roombooker.fullstack_backend.mapper.RecepcionarMapper;
import com.roombooker.fullstack_backend.model.Gost;
import com.roombooker.fullstack_backend.model.Recepcionar;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RecepcionarService {

    private final RecepcionarRepository recepcionarRepo;
    private final RecepcionarMapper recepcionarMapper;

    public RecepcionarService(RecepcionarRepository recepcionarRepo, RecepcionarMapper recepcionarMapper) {
        this.recepcionarRepo = recepcionarRepo;
        this.recepcionarMapper = recepcionarMapper;
    }

    
     //SK5 - Prijava recepcionara po korisnickom imenu i lozinci
    public RecepcionarDTO login(String email, String password) {
        RecepcionarDTO recepcionar = getRecepcionarByEmail(email);
        if (recepcionar != null && recepcionar.getLozinkaHash().equals(password)) {
            return recepcionar;
        }
        return null;
    }
// Dodatno: Dohvatanje pojedinacnog recepcionara
public RecepcionarDTO getRecepcionarByEmail(String email) {
	Recepcionar recepcionar= recepcionarRepo.findByEmail(email);
	return recepcionarMapper.toDomainDTO(recepcionar);
}
}
//    // SK10 - Dodavanje novog recepcionara
//     
//    public String addRecepcionar(RecepcionarDTO dto) {
//        if (recepcionarRepo.existsByEmail(dto.getEmail())) {
//            return "Recepcionar sa ovim email-om već postoji!";
//        }
//        Recepcionar recepcionar = recepcionarMapper.toDomainEntity(dto);
//        recepcionarRepo.save(recepcionar);
//        return "Recepcionar uspešno dodat!";
//    }
//    
//    
//     //SK11 - Izmena podataka postojeceg recepcionara
//     
//    public String updateRecepcionar(RecepcionarDTO dto) {
//        Recepcionar recepcionar = recepcionarRepo.findById(dto.getIdRecepcionar())
//                .orElseThrow(() -> new RuntimeException("Recepcionar ne postoji!"));
//        recepcionar.setIme(dto.getIme());
//        recepcionar.setPrezime(dto.getPrezime());
//        recepcionar.setEmail(dto.getEmail());
//        recepcionar.setKorisnickoIme(dto.getKorisnickoIme());
//        recepcionar.setLozinkaHash(dto.getLozinkaHash());
//        recepcionar.setTelefon(dto.getTelefon());
//        recepcionarRepo.save(recepcionar);
//        return "Recepcionar uspešno izmenjen!";
//    }
//
//   
//     // SK12 - Brisanje recepcionara
//
//    public String deleteRecepcionar(int idRecepcionar) {
//        if (!recepcionarRepo.existsById(idRecepcionar)) {
//            return "Recepcionar ne postoji!";
//        }
//        recepcionarRepo.deleteById(idRecepcionar);
//        return "Recepcionar uspešno obrisan!";
//    }
//}
