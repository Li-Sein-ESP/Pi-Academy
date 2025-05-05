package services;

import entities.Paiement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaiementService {
    private final Connection connection;

    public PaiementService(Connection connection) {
        this.connection = connection;
    }

    public List<Paiement> getAllPaiements() throws SQLException {
        List<Paiement> paiements = new ArrayList<>();
        String sql = "SELECT * FROM paiement";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Paiement paiement = extractPaiementFromResultSet(rs);
                paiements.add(paiement);
            }
        }

        return paiements;
    }

    public Paiement getPaiementById(int id) throws SQLException {
        String sql = "SELECT * FROM paiement WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractPaiementFromResultSet(rs);
                }
            }
        }

        return null;
    }

    public boolean ajouterPaiement(Paiement paiement) throws SQLException {
        String sql = "INSERT INTO paiement (montant, date, methode, id_etudiant) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDouble(1, paiement.getMontant());
            stmt.setDate(2, Date.valueOf(paiement.getDate()));
            stmt.setString(3, paiement.getMethode());
            stmt.setInt(4, paiement.getIdEtudiant());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        paiement.setId(generatedKeys.getInt(1));
                    }
                }
            }
            return rowsAffected > 0;
        }
    }

    public boolean mettreAJourPaiement(Paiement paiement) throws SQLException {
        String sql = "UPDATE paiement SET montant = ?, date = ?, methode = ?, id_etudiant = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, paiement.getMontant());
            stmt.setDate(2, Date.valueOf(paiement.getDate()));
            stmt.setString(3, paiement.getMethode());
            stmt.setInt(4, paiement.getIdEtudiant());
            stmt.setInt(5, paiement.getId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean supprimerPaiement(int id) throws SQLException {
        String sql = "DELETE FROM paiement WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    private Paiement extractPaiementFromResultSet(ResultSet rs) throws SQLException {
        Paiement paiement = new Paiement();
        paiement.setId(rs.getInt("id"));
        paiement.setMontant(rs.getDouble("montant"));
        paiement.setDate(rs.getDate("date").toLocalDate());
        paiement.setMethode(rs.getString("methode"));
        paiement.setIdEtudiant(rs.getInt("id_etudiant"));
        return paiement;
    }
}