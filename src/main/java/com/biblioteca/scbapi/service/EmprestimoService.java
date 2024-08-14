package com.biblioteca.scbapi.service;


import com.biblioteca.scbapi.exception.RegraNegocioException;
import com.biblioteca.scbapi.model.entity.Emprestimo;
import com.biblioteca.scbapi.model.repository.EmprestimoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
public class EmprestimoService {
    private EmprestimoRepository repository;

    public EmprestimoService(EmprestimoRepository repository) {
        this.repository = repository;
    }

    public List<Emprestimo> getEmprestimos() {
        return repository.findAll();
    }

    public Optional<Emprestimo> getEmprestimoById(Long id) {
        return repository.findById(id);
    }

    public Emprestimo salvar(Emprestimo emprestimo) {
        validar(emprestimo);
        validarEmprestadoOuEmAtraso(emprestimo);
        emprestimo = insertEmprestimoDates(emprestimo);
        return repository.save(emprestimo);
    }

    @Transactional
    public void excluir(Emprestimo emprestimo) {
        Objects.requireNonNull(emprestimo.getId());
        repository.delete(emprestimo);
    }

    public Emprestimo insertEmprestimoDates(Emprestimo emprestimo){

        if(emprestimo.getDataDeEmprestimo() == null){
            emprestimo.setDataDeEmprestimo(new Date());
        }

        if(emprestimo.getDataPrevistaDeDevolucao() == null){
            emprestimo.setDataDeEmprestimo(new Date((new Date()).getTime() + (7L * 24 * 60 * 60 * 1000)));
        }

        return emprestimo;
    }


    public void validar(Emprestimo emprestimo) {
        if (emprestimo.getExemplar() == null) {
            throw new RegraNegocioException("Nenhum exemplar selecionado");
        }
        if (emprestimo.getTomador() == null) {
            throw new RegraNegocioException("Tomador em branco");
        }
    }
    public void validarEmprestadoOuEmAtraso(Emprestimo emprestimo) {
       if(!emprestimo.getExemplar().isEmprestado()){
            throw new RegraNegocioException("Exemplar já emprestado");
        }
       if (emprestimo.getTomador().isEmAtarso()) {
            throw new RegraNegocioException("Tomador com pendencias");
        }
    }
}