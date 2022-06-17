package net.md_5.bungee.protocol.packet;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.md_5.bungee.protocol.AbstractPacketHandler;
import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.ProtocolConstants;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ServerData extends DefinedPacket
{

    private String motd; // nullable
    private String iconBase64; // nullable
    private boolean previewsChat;
    private boolean enforcesSecureChat;

    @Override
    public void read(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion)
    {
        if ( buf.readBoolean() )
        {
            motd = readString( buf, 262144 );
        }
        if ( buf.readBoolean() )
        {
            iconBase64 = readString( buf, 32767 );
        }
        previewsChat = buf.readBoolean();
        if ( protocolVersion >= ProtocolConstants.MINECRAFT_1_19_1 )
        {
            enforcesSecureChat = buf.readBoolean();
        }
    }

    @Override
    public void write(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion)
    {
        if ( motd != null )
        {
            buf.writeBoolean( true );
            writeString( motd, buf );
        } else
        {
            buf.writeBoolean( false );
        }
        if ( iconBase64 != null )
        {
            buf.writeBoolean( true );
            writeString( iconBase64, buf );
        } else
        {
            buf.writeBoolean( false );
        }
        buf.writeBoolean( previewsChat );
        if ( protocolVersion >= ProtocolConstants.MINECRAFT_1_19_1 )
        {
            buf.writeBoolean( enforcesSecureChat );
        }
    }

    public ServerData copy()
    {
        return new ServerData( motd, iconBase64, previewsChat, enforcesSecureChat );
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception
    {
        handler.handle( this );
    }
}
