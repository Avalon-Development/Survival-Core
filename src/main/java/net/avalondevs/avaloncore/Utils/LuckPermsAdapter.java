package net.avalondevs.avaloncore.Utils;

import lombok.experimental.UtilityClass;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.InheritanceNode;
import net.luckperms.api.node.types.MetaNode;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.Consumer;

@UtilityClass
public class LuckPermsAdapter {

    public LuckPerms luckperms;

    public void init() {

        luckperms = LuckPermsProvider.get();

    }

    public User obtainByAnyMeans(UUID uuid) {

        return luckperms.getUserManager().getUser(uuid);

    }

    public String getDefiniteName(Group group) {

        String name = group.getDisplayName();
        if (name == null)
            name = group.getFriendlyName();

        return name;

    }

    public void setMetaData(User user, String key, String value) {

        MetaNode node = MetaNode.builder(key, value).build();

        user.data().clear(NodeType.META.predicate(mn -> mn.getMetaKey().equals(key)));

        user.data().add(node);

        save(user);
    }

    @NotNull
    public String getMetaData(@NotNull User user, @NotNull String key) {

        CachedMetaData metaData = user.getCachedData().getMetaData();

        String value = metaData.getMetaValue(key);

        return value == null ? "" : value;

    }

    public void unsetMetaData(User user, String key) {

        user.data().clear(NodeType.META.predicate(mn -> mn.getMetaKey().equals(key)));

        save(user);

    }

    public void setPermission(User user, String permission, boolean value) {


        Node node = Node.builder(permission).value(value).build();

        user.data().add(node);

        save(user);


    }

    public Group getPrimaryGroup(User user) {

        return luckperms.getGroupManager().getGroup(user.getPrimaryGroup());

    }

    public void setPrimaryGroup(User user, Group group) {

        modifyUser(user.getUniqueId(), (User modifiable) -> {

            user.data().clear(NodeType.INHERITANCE::matches);

            // Create a node to add to the player.
            Node node = InheritanceNode.builder(group.getName()).build();

            // Add the node to the user.
            user.data().add(node);

        });

    }

    public Group getByName(String name) {

        return luckperms.getGroupManager().getGroup(name);

    }

    public int getWeight(Group group) {

        return group.getWeight().orElse(0);

    }

    public void save(User user) {

        luckperms.getUserManager().saveUser(user);

    }

    public void modifyUser(UUID uuid, Consumer<User> action) {

        luckperms.getUserManager().modifyUser(uuid, action);

    }

    public String getPrefix(UUID uuid) {
        String prefix = LuckPermsProvider.get().getGroupManager().getGroup(LuckPermsProvider.get().getUserManager().getUser(uuid).getPrimaryGroup()).getCachedData().getMetaData().getPrefix();
        return prefix;
    }

}
