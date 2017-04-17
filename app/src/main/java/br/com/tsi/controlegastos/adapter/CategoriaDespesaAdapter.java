package br.com.tsi.controlegastos.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.tsi.controlegastos.R;
import br.com.tsi.controlegastos.model.CategoriaDespesa;
import java.util.List;

public class CategoriaDespesaAdapter extends  RecyclerView.Adapter {

    private Context context;
    private LayoutInflater inflater;
    private List<CategoriaDespesa> listaCategoriaDespesas;

    public CategoriaDespesaAdapter(Context context, List<CategoriaDespesa> listaCategoriaDespesas)
    {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.listaCategoriaDespesas = listaCategoriaDespesas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.itemcategoriadespesa, parent, false);
        return new AndroidItemHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Log.d("Script","CategoriaDespesaAdapter.onBindViewHolder");

        AndroidItemHolder androidItemHolder = (AndroidItemHolder) holder;
        androidItemHolder.IdCategoriaDespesa.setText(Long.toString(listaCategoriaDespesas.get(position).getId()));
        androidItemHolder.tvNome.setText(listaCategoriaDespesas.get(position).getNome());

    }

    @Override
    public int getItemCount() {
        return listaCategoriaDespesas.size();
    }

    public void removeListItem(long categoriaDespesasId, int position)
    {
        listaCategoriaDespesas.remove(position);
        notifyItemRemoved(position);
    }

    private class AndroidItemHolder extends RecyclerView.ViewHolder {

        TextView tvNome;
        TextView IdCategoriaDespesa;

        public AndroidItemHolder(View itemView)  {
            super(itemView);
            tvNome = (TextView) itemView.findViewById(R.id.tvNome);
            IdCategoriaDespesa = (TextView) itemView.findViewById(R.id.tvIdCategoriaDespesa);
        }

    }

}

