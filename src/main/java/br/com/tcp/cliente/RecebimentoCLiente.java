package br.com.tcp.cliente;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

class RecebimentoClient extends ChannelInboundHandlerAdapter {

    private static String ack = "\u0006";

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            byte[] dados = byteParaString((ByteBuf) msg);
            System.out.println("Mensagem Recebida do Servidor >> " + new String(dados));
//            enviarAck(ctx);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Cliente se conectou!!!");
        System.out.println("Esperando eventos...");
        super.channelActive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    private byte[] byteParaString(ByteBuf msg) {
        ByteBuf byteBuf = msg;
        byte[] dados;
        int length = byteBuf.readableBytes();
        if (byteBuf.hasArray()) {
            dados = byteBuf.array();
        } else {
            dados = new byte[length];
            byteBuf.getBytes(byteBuf.readerIndex(), dados);
        }
        return dados;
    }
    public void enviarAck(ChannelHandlerContext ctx){
        ByteBuf out = ctx.alloc().buffer(ack.length()*2);
        out.writeBytes(ack.getBytes());
        ctx.writeAndFlush(out);
    }
}
