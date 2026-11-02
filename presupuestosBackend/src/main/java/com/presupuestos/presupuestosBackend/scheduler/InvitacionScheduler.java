package com.presupuestos.presupuestosBackend.scheduler;

import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Scheduled;
import com.presupuestos.presupuestosBackend.service.InvitacionService;

@Component
public class InvitacionScheduler {
 
    private final InvitacionService invitacionService;

    public InvitacionScheduler(InvitacionService invitacionService){

        this.invitacionService = invitacionService;
    }
    
    @Scheduled(fixedRate = 60000)
    public void comprobarInvitacionesExpiradas() {
        invitacionService.caducarInvitacionesExpiradas();
    }

}
