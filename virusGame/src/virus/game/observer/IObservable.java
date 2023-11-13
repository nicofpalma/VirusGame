package virus.game.observer;

public interface IObservable {
    public void notificarCambio(Object objeto);
    public void agregarObservador(IObservador observador);
}
