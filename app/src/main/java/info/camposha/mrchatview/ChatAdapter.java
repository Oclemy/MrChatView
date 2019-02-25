package info.camposha.mrchatview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    public static final int MESSAGE_SEND = 1;
    public static final int MESSAGE_RECEIVED = 2;
    List<ChatModel> data;
    Context context;

    public ChatAdapter(Context context, List<ChatModel> chatModels) {
        this.data = chatModels;
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ChatModel chatModel = data.get(position);

        //identify sender or receiver
        if (data.get(position).getId() == MESSAGE_SEND) {
             //check message type to change view ex: text message or image
            if (chatModel.getMessageType().equalsIgnoreCase("img")) {
                holder.linearLayout_sendMessage.setVisibility(View.GONE);
                holder.linearLayout_receivedMessage.setVisibility(View.GONE);
                holder.linearLayout_receivedPic.setVisibility(View.GONE);

                holder.linearLayout_sendPic.setVisibility(View.VISIBLE);
                holder.img_send.setImageBitmap(chatModel.getBitmap());

            } else if (chatModel.getMessageType().equalsIgnoreCase("text")) {

                holder.linearLayout_sendPic.setVisibility(View.GONE);
                holder.linearLayout_receivedMessage.setVisibility(View.GONE);
                holder.linearLayout_receivedPic.setVisibility(View.GONE);

                holder.linearLayout_sendMessage.setVisibility(View.VISIBLE);
                holder.send_message.setText(chatModel.getMessage());
                holder.send_time.setText(chatModel.getTime());
            }
        } else if (data.get(position).getId() == MESSAGE_RECEIVED) {
             // check message type to change view ex: text message or image
            if (chatModel.getMessageType().equalsIgnoreCase("img")) {
                holder.linearLayout_sendMessage.setVisibility(View.GONE);
                holder.linearLayout_receivedMessage.setVisibility(View.GONE);
                holder.linearLayout_sendPic.setVisibility(View.GONE);

                holder.linearLayout_receivedPic.setVisibility(View.VISIBLE);
                holder.img_receive.setImageBitmap(chatModel.getBitmap());

            } else if (chatModel.getMessageType().equalsIgnoreCase("text")) {

                holder.linearLayout_sendMessage.setVisibility(View.GONE);
                holder.linearLayout_sendPic.setVisibility(View.GONE);
                holder.linearLayout_receivedPic.setVisibility(View.GONE);

                holder.linearLayout_receivedMessage.setVisibility(View.VISIBLE);
                holder.receive_message.setText(chatModel.getMessage());
                holder.receive_time.setText(chatModel.getTime());
            }
        }
        /**
         * open image in dialog
         */
        holder.img_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View view = LayoutInflater.from(context).inflate(R.layout.imageviewdemo, null);
                ImageView imageView = view.findViewById(R.id.img1);
                imageView.setImageBitmap(chatModel.getBitmap());
                builder.setView(view);
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

/**
 * resceive same image in chat view click image to view on dialog
 * commented for testing
 */
       /* holder.img_receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View view = LayoutInflater.from(context).inflate(R.layout.imageviewdemo, null);
                ImageView imageView = view.findViewById(R.id.img1);
                imageView.setImageBitmap(chatModel.getBitmap());
                builder.setView(view);
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        */
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout_sendMessage, linearLayout_receivedMessage,
                linearLayout_sendPic, linearLayout_receivedPic;

        TextView send_message, send_time, receive_message,
                receive_time, send_pic_time, receive_pic_time;
        ImageView img_send, img_receive;

        public ViewHolder(View itemView) {
            super(itemView);

            linearLayout_sendMessage = itemView.findViewById(R.id.linearLayout_sendMessage);
            send_message = itemView.findViewById(R.id.send_message);
            send_time = itemView.findViewById(R.id.send_time);

            linearLayout_sendPic = itemView.findViewById(R.id.linearLayout_sendPic);
            img_send = itemView.findViewById(R.id.img_send);
            send_pic_time = itemView.findViewById(R.id.send_pic_time);

            linearLayout_receivedMessage = itemView.findViewById(R.id.linearLayout_receivedMessage);
            receive_message = itemView.findViewById(R.id.receive_message);
            receive_time = itemView.findViewById(R.id.receive_time);

            linearLayout_receivedPic = itemView.findViewById(R.id.linearLayout_receivedPic);
            img_receive = itemView.findViewById(R.id.img_receive);
            receive_pic_time = itemView.findViewById(R.id.receive_pic_time);
        }
    }
    @Override
    public int getItemViewType(int position) {
        if (data.get(position).getId() == Constants.SENDER)
            return MESSAGE_SEND;
        else
            return MESSAGE_RECEIVED;
    }
}
//end
