package br.com.alura.forum.infra;

import br.com.alura.forum.model.Role;
import br.com.alura.forum.model.User;
import br.com.alura.forum.model.topic.domain.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class TopicAclPermissionRecorderService {

    @Autowired
    private JdbcMutableAclService aclService;

    public void addPermission(Topic topic, Permission... permissions) {
        ObjectIdentityImpl identity = new ObjectIdentityImpl(topic);
        MutableAcl acl = this.aclService.createAcl(identity);

        PrincipalSid principalSid = (PrincipalSid) acl.getOwner();

        for (Permission permission : permissions) {
            acl.insertAce(acl.getEntries().size(), permission, principalSid, true);
            enterPermissionForAdmins(acl, permission);
        }
        aclService.updateAcl(acl);
    }

    private void enterPermissionForAdmins(MutableAcl acl, Permission permission) {
        GrantedAuthoritySid adminSid = new GrantedAuthoritySid(Role.ROLE_ADMIN);
        acl.insertAce(acl.getEntries().size(), permission, adminSid, true);
    }
}
