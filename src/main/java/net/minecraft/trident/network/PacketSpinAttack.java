package net.minecraft.trident.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.trident.util.EntityHelper;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author ji_GGO
 * @date 2020/03/14
 */
public class PacketSpinAttack implements IMessage {

    private int entityId;
    private boolean type;

    public PacketSpinAttack(int entityId, boolean type) {
        this.entityId = entityId;
        this.type = type;
    }

    public PacketSpinAttack() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entityId = buf.readInt();
        type = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeBoolean(type);
    }

    public static class Handler implements IMessageHandler<PacketSpinAttack, IMessage> {
        @Override
        public IMessage onMessage(PacketSpinAttack message, MessageContext context) {
            if (context.side == Side.CLIENT) {
                Minecraft.getMinecraft().addScheduledTask(() -> {
                    Entity player = Minecraft.getMinecraft().world.getEntityByID(message.entityId);
                    EntityHelper.setSpinAttack(player, message.type);
                });
            }
            return null;
        }
    }

}