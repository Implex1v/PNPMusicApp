import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faMusic, faUser} from "@fortawesome/free-solid-svg-icons";
import Link from "next/link";
import {Dropdown} from "react-bootstrap";

export default function Navbar() {
    return (
        <nav className="navbar navbar-expand-lg navbar-dark bg-black">
            <div className="container-fluid">
                <Link href="/">
                    <a className="navbar-brand">
                        <FontAwesomeIcon icon={faMusic} className="me-1" />
                        PNPMusicApp
                    </a>
                </Link>
                <ul className="navbar-nav me-auto mb-2 mb-lg-0">
                        <Dropdown className="nav-item dropdown">
                            <Dropdown.Toggle variant="link" id="dropdown-song" className="nav-link ">
                                Songs
                            </Dropdown.Toggle>
                            <Dropdown.Menu>
                                <Dropdown.Item href="/songs" className="dropdown-item">
                                    Songs
                                </Dropdown.Item>
                                <Dropdown.Item href="/songs/new" className="dropdown-item">
                                    New Song
                                </Dropdown.Item>
                            </Dropdown.Menu>
                        </Dropdown>
                </ul>
                <div className="d-flex">
                    <button className="btn btn-outline-success">
                        <FontAwesomeIcon icon={faUser} className="me-1"/>
                        Login
                    </button>
                </div>
            </div>
        </nav>
    )
}